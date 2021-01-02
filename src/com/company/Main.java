package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//系统分析与验证作业 主入口
public class Main {

    private static Map<String, Boolean> m = new HashMap<>();

    public static void main(String[] args) {
        String text = InputUtil.readFile("data/data.txt");
        assert text != null;
        String[] textArray = text.split("\n");


//	    OutputUtil.out("model check start!");
//        Model model = new Model(5);
//        model.setP(0,"a");
//        model.setP(0,"b");
//        model.setP(1,"a");
//        model.setP(2,"b");
//        model.setP(3,"a");
//        model.setP(4,"b");
//        model.addEdge(0,1);
//        model.addEdge(1,2);
//        model.addEdge(0,2);
//        model.addEdge(2,1);
//        model.addEdge(2,2);
//        model.addEdge(3,4);
//        model.addEdge(4,3);
//        List<CTL> ctlList = new ArrayList<>();
//        ctlList.add(new CTL("a"));
//        ctlList.add(new CTL("b"));
//        ctlList.add(new CTL("a0"));
//        ctlList.add(new CTL("ab1"));
//        ctlList.add(new CTL("a0b010"));
//        ctlList.add(new CTL("a2"));
//        ctlList.add(new CTL("a3"));
//        ctlList.add(new CTL("ba4"));
//        ctlList.add(new CTL("ba1a0b14"));
//        ctlList.add(new CTL("ab5"));
//        ctlList.add(new CTL("ba1a0b15"));
//        for(CTL ctl : ctlList){
//            System.out.println("CTL: " + ctl.toString());
//            for(int s = 0; s < model.getCount(); s++){
//                System.out.println("state: " + s + " result: " + model.verify(s,ctl));
//            }
//        }
//        model.printM();
    }
}
