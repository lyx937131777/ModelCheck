package com.company;

import java.util.List;

public class CheckSystem {

    private Model model;

    private CTLQuery ctlQuery;

    public CheckSystem(){

    }

    public void run(){
        String modelText = InputUtil.readFile("data/test_model.txt");
        String cltText = InputUtil.readFile("data/test_ctl.txt");
        model = new Model(modelText);
        ctlQuery = new CTLQuery(cltText);
        for (CTL ctl : ctlQuery.getCtls()) {
            System.out.println("CTL: " + ctl.toString());
            for (int s = 0; s < model.getCount(); s++) {
                System.out.println("state: " + s + " result: " + model.verify(s, ctl));
            }
        }
        System.out.println();
    }

}
