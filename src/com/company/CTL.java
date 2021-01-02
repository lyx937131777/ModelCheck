package com.company;

import java.util.Stack;

public class CTL {
    private String root;
    private CTL left;
    private CTL right;

    public CTL(CTL left, CTL right, String root) {
        this.root = root;
        this.left = left;
        this.right = right;
    }

    public CTL(String s){
        if(s == null || s.length() == 0){
            System.out.println("Construct Wrong!");
            return ;
        }
        Stack<CTL> stack = new Stack<>();
        String[] nodes = s.split(" ");
        for(int i = 0; i < nodes.length; i++){
            String root = nodes[i];
            if(isOperator(root) == 0){
                stack.push(new CTL(null,null,root));
            }else if(isOperator(root) == 1){
                CTL l = stack.pop();
                stack.push(new CTL(l,null,root));
            }else {
                CTL r = stack.pop();
                CTL l = stack.pop();
                stack.push(new CTL(l,r,root));
            }
        }
        CTL ctl = stack.pop();
        root = ctl.getRoot();
        left = ctl.getLeft();
        right = ctl.getRight();
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

    @Override
    public String toString() {
        String result = "";
        if(left != null){
            result += left.toString();
        }
        if(right != null){
            result += right.toString();
        }
        return result + root + " ";
    }



    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public CTL getLeft() {
        return left;
    }

    public void setLeft(CTL left) {
        this.left = left;
    }

    public CTL getRight() {
        return right;
    }

    public void setRight(CTL right) {
        this.right = right;
    }
}
