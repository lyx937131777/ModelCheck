package com.company;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

//有向图
public class Model {
    private static final String TRUE = "TRUE";
    private static final String FAIR = "FAIR";

    //点数(状态数) 0 到 count-1
    private int count;

    //状态+后序表达式的形式
    private Map<String, Boolean> sCTLMap = new HashMap<>();

    //邻接矩阵
    private boolean[][] m;

    //邻接矩阵的反向图
    private boolean[][] m_;

    //强连通分量
    private List<List<Integer>> components;
    //公平性状态集合
    private List<List<Integer>> fair = new ArrayList<>();


    //核心函数 对外可见
    //判断某个状态s是否含满足CTL ctl
    public boolean verify(int s, CTL ctl) {
        String sCTL = s + ctl.toString();
        if (sCTLMap.get(sCTL) != null) {
            return sCTLMap.get(sCTL);
        }//已经标记过的直接返回标记结果
        String root = ctl.getRoot();
        if (CTL.isOperator(root) == 0) {
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
            case "or": {
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
            default:
                return false;
        }
    }


    //6种算子 分别实现
    private boolean verifyNot(int s, CTL ctl) {
        return !verify(s, ctl.getLeft());
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
        CTL newCTL = new CTL(CTL.CTL_TRUE, ctl.getLeft(), "AU");
        return verify(s, newCTL);
    }

    private boolean verifyEF(int s, CTL ctl) {
        CTL newCTL = new CTL(CTL.CTL_TRUE, ctl.getLeft(), "EU");
        return verify(s, newCTL);
    }

    private boolean verifyAG(int s, CTL ctl) {
        CTL notCTL = new CTL(ctl.getLeft(), null, "not");
        CTL efCTL = new CTL(notCTL, null, "EF");
        CTL newCTL = new CTL(efCTL, null, "not");
        return verify(s, newCTL);
    }

    private boolean verifyEG(int s, CTL ctl) {
        CTL notCTL = new CTL(ctl.getLeft(), null, "not");
        CTL afCTL = new CTL(notCTL, null, "AF");
        CTL newCTL = new CTL(afCTL, null, "not");
        return verify(s, newCTL);
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
                    setP(comp.get(i), FAIR);
                    temp.add(comp.get(i));
                }
            }
        }
        for (int n : temp) {
            dfsQ(n);
        }
        for (int i = 0; i < count; ++i) {
            System.out.println(getP(i, FAIR));
        }
    }

    //辅助打Q标签的dfs
    private void dfsQ(int u) {
        for (int i = 0; i < count; ++i) {
            if (m_[u][i] && !getP(i, FAIR)) {
                setP(i, FAIR);
                dfsQ(i);
            }
        }
    }

    //TODO 文件读入 文件中原子先用abcd表示 （这是model单独用一个文件表示时使用的 我倾向于model和CTL放在一个文件输入 写在InputUtil里）
    public Model(String modelText) {
        assert modelText != null;
        String[] modelArray = modelText.split("\n");
        count = Integer.parseInt(modelArray[0]);
        setTRUE();
        m = new boolean[count][count];
        m_ = new boolean[count][count];
        //所有边数
        int edges = Integer.parseInt(modelArray[1]);
        for (int i = 2; i < edges + 2; ++i) {
            String[] node = modelArray[i].split(" ");
            int u = Integer.parseInt(node[0]);
            int v = Integer.parseInt(node[1]);
            addEdge(u, v);
        }
        //为每个状态上的原子命题打上标签
        for (int i = edges + 2; i < count + edges + 2; ++i) {
            String[] label = modelArray[i].split(" ");
            for (int j = 1; j < label.length; ++j) {
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
        this.components = new Tarjan(count, m).getComponents();
        verifyQ();
    }


    public void addEdge(int p, int q) {
        m[p][q] = true;
        m_[q][p] = true;
    }

    public int getCount() {
        return count;
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


//    public void printM() {
//        for (int i = 0; i < count; i++) {
//            for (int j = 0; j < count; j++) {
//                System.out.print(m[i][j] ? 1 : 0);
//            }
//            System.out.println();
//        }
//    }
}
