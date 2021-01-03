package com.company;

import java.util.ArrayList;
import java.util.List;

public class CTLQuery {
    private List<CTL> ctls;

    public List<CTL> getCtls() {
        return ctls;
    }

    public CTLQuery(String ctlText) {
        String[] ctlArray = ctlText.split("\n");
        ctls = new ArrayList<>();
        int qs = Integer.parseInt(ctlArray[0]);
        for (int i = 1; i <= qs; ++i) {
            ctls.add(new CTL(ctlArray[i]));
        }
    }


}
