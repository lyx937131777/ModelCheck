package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

//输出
public class OutputUtil {

    public static void writeFile(String fileName, String result){
        File file = new File(fileName);
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(result);
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void out(Object s){
        System.out.println(s);
    }
}
