package com.company;

import java.io.File;
import java.util.*;

//有向图
public class Model {
    //点数(状态数) 0 到 count-1
    private int count;

    //状态+后序表达式的形式
    private Map<String, Boolean> sCTLMap = new HashMap<>();

    //邻接矩阵
    private boolean[][] m;



    //核心函数 对外可见
    //判断某个状态s是否含满足CTL ctl
    public boolean verify(int s, CTL ctl) {
        String sCTL = s + ctl.toString();
        if (sCTLMap.get(sCTL) != null) {
            return sCTLMap.get(sCTL);
        }//已经标记过的直接返回标记结果
        String root = ctl.getRoot();
        if (isOperator(root) == 0) {
            sCTLMap.put(sCTL, false);
        } else {
            sCTLMap.put(sCTL, verify(s, ctl, root));
        }
        return sCTLMap.get(sCTL);//返回时一定对该s+CTL进行了标记
    }

    //根据不同的操作符 用不同的方法验证s是否满足CTL
    private boolean verify(int s, CTL ctl, String operator) {
        switch (operator) {
            case "not": {
                return verifyNot(s, ctl);
            }
            case "and": {
                return verifyAnd(s, ctl);
            }
            case "or":{
                return verifyOr(s,ctl);
            }
            case "AX": {
                return verifyAX(s, ctl);
            }
            case "EX": {
                return verifyEX(s, ctl);
            }
            case "AU": {
                return verifyAU(s, ctl);
            }
            case "EU": {
                return verifyEU(s, ctl);
            }
            default:
                return false;
        }
    }

    //若不为操作符返回0 否则返回x是几元操作符
    private int isOperator(String root){
        //TODO 可能要增加
        switch (root){
            case "not":
            case "AX":
            case "EX":{
                return 1;
            }
            case "and":
            case "or":
            case "AU":
            case "EU": {
                return 2;
            }
            default:{
                return 0;
            }
        }
    }

    //6种算子 分别实现
    private boolean verifyNot(int s, CTL ctl) {
        return !verify(s, ctl.getLeft());
    }

    private boolean verifyAnd(int s, CTL ctl) {
        return verify(s, ctl.getLeft()) && verify(s, ctl.getRight());
    }

    private boolean verifyOr(int s, CTL ctl){
        return verify(s, ctl.getLeft()) || verify(s, ctl.getRight());
    }
    private boolean verifyAX(int s, CTL ctl) {
        for (int t = 0; t < count; t++) {
            if (m[s][t] && !verify(t, ctl.getLeft())) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyEX(int s, CTL ctl) {
        for (int t = 0; t < count; t++) {
            if (m[s][t] && verify(t, ctl.getLeft())) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyAU(int s, CTL ctl) {
        boolean[] visited = new boolean[count];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            String uCTL = u + ctl.toString();
            if (sCTLMap.get(uCTL) != null) {
                if (sCTLMap.get(uCTL)) {
                    continue;
                }
                return false;
            }
            if (visited[u]) {
                sCTLMap.put(uCTL, false);
                return false;
            }
            visited[u] = true;
            if (verify(u, ctl.getRight())) {
                sCTLMap.put(uCTL, true);
                continue;
            }
            if (!verify(u, ctl.getLeft())) {
                sCTLMap.put(uCTL, false);
                return false;
            }
            for (int v = 0; v < count; v++) {
                if (m[u][v]) {
                    queue.offer(v);
                }
            }
        }
        return true;
    }

    private boolean verifyEU(int s, CTL ctl) {
        boolean[] visited = new boolean[count];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            String uCTL = u + ctl.toString();
            if (sCTLMap.get(uCTL) != null) {
                if (sCTLMap.get(uCTL)) {
                    return true;
                }
                continue;
            }
            if (visited[u]) {
                continue;
            }
            visited[u] = true;
            if (verify(u, ctl.getRight())) {
                sCTLMap.put(uCTL, true);
                return true;
            }
            if (!verify(u, ctl.getLeft())) {
                sCTLMap.put(uCTL, false);
                continue;
            }
            for (int v = 0; v < count; v++) {
                if (m[u][v]) {
                    queue.offer(v);
                }
            }
        }
        return false;
    }

    private void verifyQ() {

    }

    //TODO 文件读入 文件中原子先用abcd表示 （这是model单独用一个文件表示时使用的 我倾向于model和CTL放在一个文件输入 写在InputUtil里）
    public Model(String modelText) {
        assert modelText != null;
        String[] modelArray = modelText.split("\n");
        count = Integer.parseInt(modelArray[0]);
        m = new boolean[count][count];
        for (int i = 0; i < count; ++i) {
            sCTLMap.put(i + "TRUE ", true);
        }
        int edges = Integer.parseInt(modelArray[1]);
        for (int i = 2; i < edges + 2; ++i) {
            String[] node = modelArray[i].split(" ");
            m[Integer.parseInt(node[0])][Integer.parseInt(node[1])] = true;
        }
        for (int i = edges + 2; i < count + edges + 2; ++i) {
            String[] label = modelArray[i].split(" ");
            for (int j = 1; j < label.length; ++j) {
                sCTLMap.put(label[0] + label[j] + " ", true);
            }
        }

    }


    public void addEdge(int p, int q) {
        m[p][q] = true;
    }


    public int getCount() {
        return count;
    }

    public void setP(int s, String a) {
        sCTLMap.put(s + a, true);
    }

    public void printM() {
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                System.out.print(m[i][j] ? 1 : 0);
            }
            System.out.println();
        }
    }
}
