package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//输入
public class InputUtil {
    //TODO 文件读入 文件中原子先用abcd表示 CTL中算子也用0123表示
    //TODO 我倾向于model和CTL在同一个文件中输入 前面是model 后面再一个数字m表示CTL的个数 后面m行CTL
    public static String readFile(String fileName) {
        File f = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
            String s = null;
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
