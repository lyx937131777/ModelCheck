package com.company;

import java.io.*;
import java.util.Scanner;

//输入
public class InputUtil {

    public static String readLine(){
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

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
        } catch (FileNotFoundException e) {
            System.out.println("文件名不存在");
            return null;
        }catch (IOException e){
            System.out.println("IO错误");
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
