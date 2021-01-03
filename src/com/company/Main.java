package com.company;

import java.lang.reflect.Array;
import java.util.*;

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
        String name = InputUtil.readLine();
        while( !name.equals("quit")){
            CheckSystem checkSystem = new CheckSystem(name);
            checkSystem.run();
            name = InputUtil.readLine();
        }
    }
}
