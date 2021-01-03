package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for (int i = 0; i < ctlQuery.getCtls().size(); i++) {
            CTL ctl = ctlQuery.getCtls().get(i);
            System.out.println(i + " CTL: " + ctl.inOrder());
            for (int s = 0; s < model.getCount(); s++) {
                printResult(s,model.verify(s,ctl),modelF.verify(s,ctl));
            }
        }
        System.out.println("CheckSystem: \"" + name + "\" end!");
        System.out.println("========================================");
        output();
        paint();
    }

    public void output(){
        outputResult();
        outputLabel();
    }

    public void outputLabel(){
        String labelResult = "";
        Map<String,Boolean> sCTLMap = modelF.getsCTLMap();
        List<String>[] labelLists = new List[modelF.getCount()];
        for(int i = 0; i < modelF.getCount(); i++){
            labelLists[i] = new ArrayList<>();
        }
        for(Map.Entry<String,Boolean> entry : sCTLMap.entrySet()){
            if(entry.getValue()){
                String key = entry.getKey();
                int s = key.charAt(0) - '0';
                labelLists[s].add(key.substring(1));
            }
        }
        for(int i = 0; i < modelF.getCount(); i++){
            labelResult += i + "\n";
            for(int j =0; j < labelLists[i].size(); j++){
                labelResult += labelLists[i].get(j) + "\n";
            }
            labelResult += "\n";
        }
        OutputUtil.writeFile(getFileName("out_label"),labelResult);
    }

    public void outputResult(){
        String result = "";
        for (int i = 0; i < ctlQuery.getCtls().size(); i++) {
            CTL ctl = ctlQuery.getCtls().get(i);
            result += i + " CTL：" + ctl.inOrder() + "\n";
            for (int s = 0; s < model.getCount(); s++) {
                result += getResult(s,model.verify(s,ctl),modelF.verify(s,ctl));
            }
        }
        OutputUtil.writeFile(getFileName("out_result"),result);
    }

    public void paint(){
        new MFrame(name,modelF);
    }

    private void printResult(int s, boolean result, boolean resultWithFair){
        System.out.print(getResult(s,result,resultWithFair));
    }

    private String getResult(int s, boolean result, boolean resultWithFair){
        return "state: " + s + " result: " + (result?" True":"False")
                + ", result with fair: " + (resultWithFair?" True":"False") + ".  "
                + ((result == resultWithFair)?"Same":"Different") + "\n";
    }

    private String getFileName(String type){
        return "data/" + name + "/" + type + ".txt";
    }

}
