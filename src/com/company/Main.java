package com.company;

import java.util.*;

//系统分析与验证作业 主入口
public class Main {

    private static Map<String, Boolean> m = new HashMap<>();

    public static void main(String[] args) {
        String modelText = InputUtil.readFile("data/test_model.txt");
        String cltText = InputUtil.readFile("data/test_ctl.txt");
        Model model = new Model(modelText);
        CTLQuery query = new CTLQuery(cltText);
        for (CTL ctl : query.getCtls()) {
            System.out.println("CTL: " + ctl.toString());
            for (int s = 0; s < model.getCount(); s++) {
                System.out.println("state: " + s + " result: " + model.verify(s, ctl));
            }
        }
        model.printM();
    }
}
