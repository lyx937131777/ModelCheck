package com.company;

//系统分析与验证作业 主入口
public class Main {

    public static void main(String[] args) {
//        CheckSystem checkSystemTest = new CheckSystem("test");
//        checkSystemTest.run();
//        CheckSystem checkSystemTest2 = new CheckSystem("test2");
//        checkSystemTest2.run();
//        CheckSystem checkSystemMEP = new CheckSystem("mutual_exclusion_problem");
//        checkSystemMEP.run();
//        CheckSystem checkSystemABP = new CheckSystem("ABP");
//        checkSystemABP.run();
//        CheckSystem checkSystemABP8State = new CheckSystem("ABP8State");
//        checkSystemABP8State.run();
//        CheckSystem checkSystemABPTest = new CheckSystem("ABPtest");
//        checkSystemABPTest.run();
        System.out.print("输入要验证的系统名称：");
        String name = InputUtil.readLine();
        while( !name.equals("quit")){
            CheckSystem checkSystem = new CheckSystem(name);
            checkSystem.run();
            System.out.print("输入要验证的系统名称：");
            name = InputUtil.readLine();
        }
    }
}
