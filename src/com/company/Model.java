package com.company;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean [][] m = new boolean[100][100];

    //判断某个状态s是否含满足CTL ctl
    public boolean verify(int s,CTL ctl){
        String sCTL = s + ctl.toString();
        if(sCTLMap.get(sCTL) != null){
            return sCTLMap.get(sCTL);
        }
        String root = ctl.getRoot();
        if(isOperator(root.charAt(0)) == 0){
            sCTLMap.put(sCTL,false);
        }else {
            sCTLMap.put(sCTL,verify(s,ctl,Integer.valueOf(root)));
        }
        return sCTLMap.get(sCTL);
    }

    private boolean verify(int s, CTL ctl, int operator){
        switch (operator){
            case 0:{
                return verifyNot(s,ctl);
            }
            case 1:{
                return verifyAnd(s,ctl);
            }
            default:
                return false;
        }
    }

    //若不为操作符返回0 否则返回x是几元操作符
    private int isOperator(char x){
        //TODO 可能要增加
        if(x == '0' || x == '4' || x =='5'){
            return 1;
        }else if(x == '1' || x == '2' || x == '3' ){
            return 2;
        }
        return 0;
    }

    private boolean verifyNot(int s, CTL ctl){
        return !verify(s,ctl.getLeft());
    }
    public boolean verifyAnd(int s, CTL ctl){
        return verify(s,ctl.getLeft()) && verify(s,ctl.getRight());
    }

    public Model(File f){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Model(){

    }

    public void addEdge(int p, int q){
        m[p][q] = true;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void setP(int s, String a){
        sCTLMap.put(s+a,true);
    }
}
