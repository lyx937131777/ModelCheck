package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.List;

public class MFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 100;
    private static final int HEIGHT = 70;

    private static final double TAN = (double)HEIGHT / WIDTH;

    private static final int ARC_WIDTH = 12;
    private static final int ARC_HEIGHT = 12;

    private ModelF modelF;

    class MPane extends JPanel{

        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics gp){
            super.paint(gp);

            Graphics2D gp2d = (Graphics2D) gp;
            //绘制椭圆的边框。
            gp2d.setColor(Color.black);

            //画状态并标记P
            List<String>[] pList = modelF.getPList();
            for(int i = 0; i < modelF.getCount(); i++){
                int x = getCornerX(i);
                int y = getCornerY(i);
                gp2d.drawRoundRect(x,y,MFrame.WIDTH,MFrame.HEIGHT,ARC_WIDTH,ARC_HEIGHT);
                for(int j = 0; j < pList[i].size(); j++){
                    gp2d.drawString(pList[i].get(j),x+10,y + 20 + j * 20);
                }
            }

            //画边
            for(int i = 0; i < modelF.getCount(); i++){
                for(int j =0; j < modelF.getCount(); j++){
                    if(modelF.hasEdge(i,j)){
                        if(i != j){
                            drawEdge(gp2d,i,j);
                        }else {
                            gp2d.drawOval(getCornerX(i)-15,getCornerY(i)-15,30,30);
                        }
                    }
                }
            }

            //编号
            gp2d.setColor(Color.red);
            for(int i = 0; i < modelF.getCount(); i++){
                int x = getCenterX(i);
                int y = getCenterY(i);
                gp2d.drawString(String.valueOf(i),x-5,y+50);
            }
        }
    }

    public MFrame(String name, ModelF modelF){
        this.modelF = modelF;
        this.setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置窗体关闭模式

        setContentPane(new MPane());

        this.setTitle(name + "模型状态图");

        this.setLocationRelativeTo(null);//窗体居中

        this.setVisible(true);//设置窗体的可见性

    }

    private int getCornerX(int i){
        return  200 + i%2 * 600 - i/2%2 * 100;
    }

    private int getCornerY(int i){
        return 30 + i * 70;
    }

    private int getCenterX(int i){
        return getCornerX(i) + WIDTH/2;
    }

    private int getCenterY(int i){
        return getCornerY(i) + HEIGHT/2;
    }

    private void drawEdge(Graphics2D gp2d, int i, int j){
        int sx = getCenterX(i);
        int sy = getCenterY(i);
        int ex = getCenterX(j);
        int ey = getCenterY(j);
        double tan = 1000.0;
        if(ex != sx){
            tan = (double)(ey - sy) / (ex - sx);
        }
        if(tan > -TAN && tan < TAN){
            if(ex > sx){
                ex -= WIDTH/2 + 2;
                sx += WIDTH/2 + 2;
                sy += tan * (WIDTH/2 + 2);
                ey -= tan * (WIDTH/2 + 2);
            }else {
                ex += WIDTH/2 + 2;
                sx -= WIDTH/2 + 2;
                sy -= tan * (WIDTH/2 + 2);
                ey += tan * (WIDTH/2 + 2);
            }
        }else {
            if(ey > sy){
                ey -= HEIGHT/2 + 2;
                sy += HEIGHT/2 + 2;
                sx += (HEIGHT/2 + 2) / tan;
                ex -= (HEIGHT/2 + 2) / tan;
            }else{
                ey += HEIGHT/2 + 2;
                sy -= HEIGHT/2 + 2;
                sx -= (HEIGHT/2 + 2) / tan;
                ex += (HEIGHT/2 + 2) / tan;
            }
        }
        drawLineWithArrow(gp2d,sx,sy,ex,ey);
    }

    private void drawLineWithArrow(Graphics2D gp2d, int sx, int sy, int ex, int ey){
        gp2d.drawLine(sx,sy,ex,ey);
        gp2d.fill(getTriangle(sx,sy,ex,ey));
    }

    private static GeneralPath getTriangle(int sx, int sy, int ex, int ey){
        double H = 10; // 箭头高度
        double L = 4; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];

        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        GeneralPath triangle = new GeneralPath();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.closePath();
        return triangle;
    }

    // 计算
    public static double[] rotateVec(int px, int py, double ang,
                                     boolean isChLen, double newLen) {

        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }
}
