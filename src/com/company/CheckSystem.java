package com.company;

public class CheckSystem {

    private String name;

    private Model model;
    private ModelF modelF;

    private CTLQuery ctlQuery;

    public CheckSystem(String name){
        this.name = name;
        String modelText = InputUtil.readFile(getFileName("model"));
        String cltText = InputUtil.readFile(getFileName("ctl"));
        if(modelText != null){
            model = new Model(modelText);
            modelF = new ModelF(modelText);
        }
        if(cltText != null){
            ctlQuery = new CTLQuery(cltText);
        }
    }

    public void run(){
        if(model == null || modelF == null || ctlQuery == null){
            return;
        }
        System.out.println("========================================");
        System.out.println("CheckSystem: \"" + name + "\" start!");
        for (CTL ctl : ctlQuery.getCtls()) {
            ctl.print();
            for (int s = 0; s < model.getCount(); s++) {
                printResult(s,model.verify(s,ctl),modelF.verify(s,ctl));
            }
        }
        System.out.println("CheckSystem: \"" + name + "\" end!");
        System.out.println("========================================");
    }

    public void output(){

    }
    private void printResult(int s, boolean result, boolean resultWithFair){
        System.out.print("state: " + s + " result: " + (result?" True":"False"));
        System.out.print(", result with fair: " + (resultWithFair?" True":"False") + ".  ");
        System.out.println((result == resultWithFair)?"Same":"Different");
    }

    private String getFileName(String type){
        return "data/" + name + "/" + type + ".txt";
    }

}
