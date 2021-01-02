package com.company;

import java.util.ArrayList;
import java.util.List;

//TODO 暂定 语法分析等的时候可能用得上
public class CTLQuery {
    private List<CTL> ctls;
//    private List<Integer> status;
//
//    public List<Integer> getStatus() {
//        return status;
//    }


    public List<CTL> getCtls() {
        return ctls;
    }

    public CTLQuery(String ctlText) {
        String[] ctlArray = ctlText.split("\n");
        ctls = new ArrayList<>();
//        status = new ArrayList<>();
        int qs = Integer.parseInt(ctlArray[0]);
        for (int i = 1; i <= qs; ++i) {
            ctls.add(new CTL(ctlArray[i]));
//            status.add(Integer.parseInt(q[1]));
        }
    }


}
