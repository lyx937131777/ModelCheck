package com.company;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

//有向图
public class Model {
    private static final String TRUE = "TRUE";
    private static final String FAIR = "FAIR";

    //S 点数(状态数) 0 到 count-1
    private int count;

    //状态+后序表达式的形式
    private Map<String, Boolean> sCTLMap = new HashMap<>();

    //R 邻接矩阵
    private boolean[][] m;

    //P
    private List<String>[] pList;

    //F 公平性状态集合
    private List<List<Integer>> fair = new ArrayList<>();

    //核心函数 对外可见
    //判断某个状态s是否含满足CTL ctl.txt
    public boolean verify(int s, CTL ctl) {
        String sCTL = s + ctl.toString();
        if (sCTLMap.get(sCTL) != null) {
            return sCTLMap.get(sCTL);
        }//已经标记过的直接返回标记结果
        String root = ctl.getRoot();
        setLabel(s,ctl,verify(s,ctl,root));
        return sCTLMap.get(sCTL);//返回时一定对该s+CTL进行了标记
    }

    //根据不同的操作符 用不同的方法验证s是否满足CTL
    private boolean verify(int s, CTL ctl, String operator) {
        switch (operator) {
            case "not":
            case "!":{
                return verifyNot(s, ctl);
            }
            case "and":
            case "&":{
                return verifyAnd(s, ctl);
            }
            case "or":
            case "|":{
                return verifyOr(s, ctl);
            }
            case "->": {
                return verifyImplication(s, ctl);
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
            case "AF": {
                return verifyAF(s, ctl);
            }
            case "EF": {
                return verifyEF(s, ctl);
            }
            case "AG": {
                return verifyAG(s, ctl);
            }
            case "EG": {
                return verifyEG(s, ctl);
            }
            default:{
                return verifyRoot(s,ctl);
            }
        }
    }


    //12种算子 分别实现
    private boolean verifyRoot(int s, CTL ctl){
        return hasTrueLabel(s,ctl);
    }

    private boolean verifyNot(int s, CTL ctl) {
        return !verify(s, ctl.getRight());
    }

    private boolean verifyAnd(int s, CTL ctl) {
        return verify(s, ctl.getLeft()) && verify(s, ctl.getRight());
    }

    private boolean verifyOr(int s, CTL ctl) {
        return verify(s, ctl.getLeft()) || verify(s, ctl.getRight());
    }

    private boolean verifyImplication(int s, CTL ctl) {
        return !verify(s, ctl.getLeft()) || verify(s, ctl.getRight());
    }

    private boolean verifyAF(int s, CTL ctl) {
        CTL newCTL = new CTL(CTL.CTL_TRUE, ctl.getRight(), "AU");
        return verify(s, newCTL);
    }

    private boolean verifyEF(int s, CTL ctl) {
        CTL newCTL = new CTL(CTL.CTL_TRUE, ctl.getRight(), "EU");
        return verify(s, newCTL);
    }

    private boolean verifyAG(int s, CTL ctl) {
        CTL notCTL = new CTL(null, ctl.getRight(), "not");
        CTL efCTL = new CTL(null, notCTL, "EF");
        CTL newCTL = new CTL(null, efCTL, "not");
        return verify(s, newCTL);
    }

    private boolean verifyEG(int s, CTL ctl) {
        CTL notCTL = new CTL(null, ctl.getRight(), "not");
        CTL afCTL = new CTL(null, notCTL, "AF");
        CTL newCTL = new CTL(null, afCTL, "not");
        return verify(s, newCTL);
    }

    private boolean verifyAX(int s, CTL ctl) {
        for (int t = 0; t < count; t++) {
            if (m[s][t] && !verify(t, ctl.getRight())) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyEX(int s, CTL ctl) {
        for (int t = 0; t < count; t++) {
            if (m[s][t] && verify(t, ctl.getRight())) {
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
                setLabel(u,ctl,false);
                return false;
            }
            visited[u] = true;
            if (verify(u, ctl.getRight())) {
                setLabel(u,ctl,true);
                continue;
            }
            if (!verify(u, ctl.getLeft())) {
                setLabel(u,ctl,false);
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
                setLabel(u,ctl,true);
                return true;
            }
            if (!verify(u, ctl.getLeft())) {
                setLabel(u,ctl,false);
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

    private void verifyQ(List<List<Integer>> components, boolean[][] m0, String Q) {
        List<Integer> temp = new ArrayList<>();
        // 遍历所有强连通分量
        for (List<Integer> comp : components) {
            boolean flag = true;
            for (List<Integer> s : fair) {
                boolean notEmpty = false;
                for (int i = 0; i < comp.size(); ++i) {
                    for (int j = 0; j < s.size(); ++j) {
                        if (comp.get(i).intValue() == s.get(j).intValue()) {
                            notEmpty = true;
                            break;
                        }
                    }
                    if (notEmpty) {
                        break;
                    }
                }
                if (!notEmpty) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (int i = 0; i < comp.size(); ++i) {
                    setP(comp.get(i), Q);
                    temp.add(comp.get(i));
                }
            }
        }
        for (int n : temp) {
            dfsQ(n, m0, Q);
        }
//        for (int i = 0; i < count; ++i) {
//            System.out.println(getP(i, FAIR));
//        }
    }

    //辅助打Q标签的dfs
    private void dfsQ(int u,boolean[][] m0, String Q) {
        for (int i = 0; i < count; ++i) {
            if (m0[i][u] && !getP(i, Q)) {
                setP(i, Q);
                dfsQ(i,m0,Q);
            }
        }
    }

    public Model(String modelText) {
        assert modelText != null;
        String[] modelArray = modelText.split("\n");
        count = Integer.parseInt(modelArray[0]);
        setTRUE();
        m = new boolean[count][count];
        //所有边数
        int edges = Integer.parseInt(modelArray[1]);
        for (int i = 2; i < edges + 2; ++i) {
            String[] node = modelArray[i].split(" ");
            int u = Integer.parseInt(node[0]);
            int v = Integer.parseInt(node[1]);
            addEdge(u, v);
        }
        //为每个状态上的原子命题打上标签
        pList = new List[count];
        for (int i = edges + 2; i < count + edges + 2; ++i) {
            String[] label = modelArray[i].split(" ");
            pList[i-edges-2] = new ArrayList<>();
            for (int j = 1; j < label.length; ++j) {
                pList[i-edges-2].add(label[j]);
                setP(Integer.parseInt(label[0]), label[j]);
            }
        }
        //公平性F的个数
        int fs = Integer.parseInt(modelArray[2 + edges + count]);
        //读入所有的公平性集合
        for (int i = count + edges + 3; i < fs + count + edges + 3; ++i) {
            String[] status = modelArray[i].split(" ");
            List<Integer> list = new ArrayList();
            for (int j = 0; j < status.length; ++j) {
                list.add(Integer.parseInt(status[j]));
            }
            fair.add(list);
        }
        //获取所有强连通分量
        List<List<Integer>> components = new Tarjan(count, m).getComponents();
        verifyQ(components,m,FAIR);
    }


    public void addEdge(int p, int q) {
        m[p][q] = true;
    }

    public int getCount() {
        return count;
    }

    public boolean hasEdge(int p, int q){
        return m[p][q];
    }

    private void setTRUE() {
        for (int s = 0; s < count; s++) {
            setP(s, "TRUE");
        }
    }

    private void printArray(List<List<Integer>> list) {
        for (int i = 0; i < list.size(); ++i) {
            for (int j : list.get(i)) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    private void setP(int s, String a) {
        sCTLMap.put(s + a + " ", true);
    }

    private boolean getP(int s, String a) {
        return sCTLMap.get(s + a + " ") != null ? sCTLMap.get(s + a + " ") : false;
    }

    private void setLabel(int s, CTL ctl, boolean b){
        sCTLMap.put(s + ctl.toString(), b);
    }

    private boolean hasTrueLabel(int s, CTL ctl){
        return sCTLMap.get(s + ctl.toString()) != null ? sCTLMap.get(s + ctl.toString()):false;
    }

    public List<String>[] getPList() {
        return pList;
    }
//    public void printM() {
//        for (int i = 0; i < count; i++) {
//            for (int j = 0; j < count; j++) {
//                System.out.print(m[i][j] ? 1 : 0);
//            }
//            System.out.println();
//        }
//    }
}
