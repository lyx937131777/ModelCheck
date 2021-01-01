package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//系统分析与验证作业 主入口
public class Main {

    private static Map<String,Boolean> m = new HashMap<>();

    public static void main(String[] args) {
	    OutputUtil.out("model check start!");
        Model model = new Model();
        model.setCount(3);
        model.setP(0,"a");
        model.setP(0,"b");
        model.setP(1,"a");
        model.setP(2,"b");
        List<CTL> ctlList = new ArrayList<>();
        ctlList.add(new CTL("a"));
        ctlList.add(new CTL("b"));
        ctlList.add(new CTL("a0"));
        ctlList.add(new CTL("ab1"));
        ctlList.add(new CTL("a0b010"));
        for(CTL ctl : ctlList){
            System.out.println("CTL: " + ctl.toString());
            for(int s = 0; s < 3; s++){
                System.out.println("state: " + s + " result: " + model.verify(s,ctl));
            }
        }
    }
}
