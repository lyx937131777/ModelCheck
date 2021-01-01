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
        for(int i = 0; i < s.length(); i++){
            char x = s.charAt(i);
            String root = String.valueOf(x);
            if(isOperator(x) == 0){
                stack.push(new CTL(null,null,root));
            }else if(isOperator(s.charAt(i)) == 1){
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

    //not  0
    //and 1
    //AX 2
    //EX 3
    //AU 4
    //EU 5
    //若不为操作符返回0 否则返回x是几元操作符
    private int isOperator(char x){
        //TODO 可能要增加
        if(x == '0' || x == '2' || x =='3'){
            return 1;
        }else if(x == '1' || x == '4' || x == '5' ){
            return 2;
        }
        return 0;
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
        return result + root;
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
