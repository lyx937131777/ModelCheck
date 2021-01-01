package com.company;

import java.io.File;
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

    //label 状态 -》 原子
//    private boolean [][] L;
    //状态+后序表达式的形式
    //not  0
    //and 1
    //AX 2
    //EX 3
    //AU 4
    //EU 5
    private Map<String,Integer> funcMap;
    //0 pq10 , 1
    //


    //邻接矩阵
    private boolean [][] m;

    //判断某个状态s是否含有某个原子ap
    public boolean label(int s,String p){
        String myP = apMap.get(p);
        String myS = s+myP;
        return funcMap.get(myP) == 1;
    }

    public boolean andLabel(int s, String p, String q){
        String myS = s + p + q + "1";
        int result = funcMap.get(myS);
        if(result == 1){
            return true
        }else if(result == -1){
            return false;
        }else {
            boolean r = label(s,p) && label(s,q);
            funcMap.put(myS,r);
            return r;
        }
    }

    public Model(File f){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
