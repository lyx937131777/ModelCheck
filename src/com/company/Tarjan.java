package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tarjan {
    //状态数量
    private int count;
    //图的邻接矩阵
    private boolean[][] m;
    //储存所有强连通分量的列表
    private List<List<Integer>> components;
    //记录顶点是否在堆栈中
    private boolean[] ins;
    //记录每个顶点的dfn
    private int[] dfn;
    //记录每个顶点的low
    private int[] low;
    //记录属于每个顶点属于哪个强连通分量
    private int[] belong;
    //记录当前的编号
    private int idx = 0;
    //强连通分量数量
    private int Bcnt = 0;
    //保存状态的堆栈
    private Stack<Integer> s;


    public Tarjan(int count, boolean[][] m) {
        this.count = count;
        this.m = m;
        components = new ArrayList();
        s = new Stack<>();
        ins = new boolean[count];
        dfn = new int[count];
        low = new int[count];
        belong = new int[count];
        for (int i = 0; i < count; ++i) {
            if (dfn[i] == 0) {
                tarjan(i);
            }
        }
        for (int i = 0; i < Bcnt; ++i) {
            components.add(new ArrayList<>());
        }
        for (int i = 0; i < count; ++i) {
            components.get(belong[i]).add(i);
        }
    }


    private void tarjan(int u) {
        //每次dfs，u的次序号增加1
        dfn[u] = low[u] = idx++;
        //u入栈并且标记
        s.push(u);
        ins[u] = true;
        //访问从u出发能到达的边
        for (int i = 0; i < count; ++i) {
            if (m[u][i]) {
                if (dfn[i] == 0) {
                    tarjan(i);
                    //u点能到达的最小次序号是它自己能到达点的最小次序号和连接点v能到达点的最小次序号中较小的
                    low[u] = Math.min(low[u], low[i]);
                } else if (ins[i]) {
                    //如果v在栈内，u点能到达的最小次序号是它自己能到达点的最小次序号和v的次序号中较小的
                    low[u] = Math.min(low[u], dfn[i]);
                }
            }
        }
        int v;
        if (dfn[u] == low[u]) {
            do {
                v = s.pop();
                ins[v] = false;
                belong[v] = Bcnt;
            } while (u != v);
            ++Bcnt;
        }
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    private void print() {
        for (int i = 0; i < components.size(); ++i) {
            for (int j : components.get(i)) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }
}
