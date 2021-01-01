package com.company;

import java.io.File;
import java.util.*;

//有向图
public class Model {
    //点(状态) 0 到 count-1
    private int count;
    private List<Integer> S0;

    //原子 规定原子不会出现数字
    private List<String> apList;
    private Map<String,String> apMap;

    //状态+后序表达式的形式
    //not  0
    //and 1
    //AX 2
    //EX 3
    //AU 4
    //EU 5
    private Map<String,Boolean> sCTLMap = new HashMap<>();

    //邻接矩阵
    private boolean [][] m;

    //核心函数 对外可见
    //判断某个状态s是否含满足CTL ctl
    public boolean verify(int s,CTL ctl){
        String sCTL = s + ctl.toString();
        if(sCTLMap.get(sCTL) != null){
            return sCTLMap.get(sCTL);
        }//已经标记过的直接返回标记结果
        String root = ctl.getRoot();
        if(isOperator(root.charAt(0)) == 0){
            sCTLMap.put(sCTL,false);
        }else {
            sCTLMap.put(sCTL,verify(s,ctl,Integer.valueOf(root)));
        }
        return sCTLMap.get(sCTL);//返回时一定对该s+CTL进行了标记
    }

    //根据不同的操作符 用不同的方法验证s是否满足CTL
    private boolean verify(int s, CTL ctl, int operator){
        switch (operator){
            case 0:{
                return verifyNot(s,ctl);
            }
            case 1:{
                return verifyAnd(s,ctl);
            }
            case 2:{
                return verifyAX(s,ctl);
            }
            case 3:{
                return verifyEX(s,ctl);
            }
            case 4:{
                return verifyAU(s,ctl);
            }
            case 5:{
                return verifyEU(s,ctl);
            }
            default:
                return false;
        }
    }

    //若不为操作符返回0 否则返回x是几元操作符
    private int isOperator(char x){
        //TODO 可能要增加
        if(x == '0' || x == '2' || x =='3'){
            return 1;
        }else if(x == '1' || x == '4' || x == '5' ){
            return 2;
        }
        return 0;
    }

    //6种算子 分别实现
    private boolean verifyNot(int s, CTL ctl){
        return !verify(s,ctl.getLeft());
    }

    private boolean verifyAnd(int s, CTL ctl){
        return verify(s,ctl.getLeft()) && verify(s,ctl.getRight());
    }

    private boolean verifyAX(int s, CTL ctl){
        for(int t = 0; t < count; t++){
            if(m[s][t] && !verify(t,ctl.getLeft())){
                return false;
            }
        }
        return true;
    }

    private boolean verifyEX(int s, CTL ctl){
        for(int t = 0; t < count; t++){
            if(m[s][t] && verify(t,ctl.getLeft())){
                return true;
            }
        }
        return false;
    }

    private boolean verifyAU(int s, CTL ctl){
        boolean [] visited = new boolean[count];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()){
            int u = queue.poll();
            String uCTL = u + ctl.toString();
            if(sCTLMap.get(uCTL) != null){
                if(sCTLMap.get(uCTL)){
                    continue;
                }
                return false;
            }
            if(visited[u]){
                sCTLMap.put(uCTL,false);
                return false;
            }
            visited[u] = true;
            if(verify(u,ctl.getRight())){
                sCTLMap.put(uCTL,true);
                continue;
            }
            if(!verify(u,ctl.getLeft())){
                sCTLMap.put(uCTL,false);
                return false;
            }
            for(int v = 0; v < count; v++){
                if(m[u][v]){
                    queue.offer(v);
                }
            }
        }
        return true;
    }

    private boolean verifyEU(int s, CTL ctl){
        boolean [] visited = new boolean[count];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()){
            int u = queue.poll();
            String uCTL = u + ctl.toString();
            if(sCTLMap.get(uCTL) != null){
                if(sCTLMap.get(uCTL)){
                    return true;
                }
                continue;
            }
            if(visited[u]){
                continue;
            }
            visited[u] = true;
            if(verify(u,ctl.getRight())){
                sCTLMap.put(uCTL,true);
                return true;
            }
            if(!verify(u,ctl.getLeft())){
                sCTLMap.put(uCTL,false);
                continue;
            }
            for(int v = 0; v < count; v++){
                if(m[u][v]){
                    queue.offer(v);
                }
            }
        }
        return false;
    }

    //TODO 文件读入 文件中原子先用abcd表示 （这是model单独用一个文件表示时使用的 我倾向于model和CTL放在一个文件输入 写在InputUtil里）
    public Model(File f){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Model(int count){
        this.count = count;
        for(int s = 0; s < count; s++){
            sCTLMap.put(s+"T",true);
        }
        m = new boolean[count][count];
    }

    public void addEdge(int p, int q){
        m[p][q] = true;
    }


    public int getCount(){
        return count;
    }

    public void setP(int s, String a){
        sCTLMap.put(s+a,true);
    }

    public void printM(){
        for(int i = 0; i < count; i++){
            for(int j = 0; j < count; j++){
                System.out.print(m[i][j]?1:0);
            }
            System.out.println();
        }
    }
}
