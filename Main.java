package com.gpx;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Point3D> CP = Point3D.createPointList("C:\\開発環境\\WebDev\\TimetrialWorks\\src\\com\\company\\sample.gpx");
        ArrayList<Segment> P = Segment.createCource(CP);
        P = Segment.concatDefault(P);



        double y = 0;
        int count=0;

        for (Segment a : P) {
            y += a.dist;
            count++;
            System.out.println(a.toString());
        }

        System.out.println(count);




        }




	// write your code here
    }

    /*public static double calctime(double dist, double weight, double w, double drop , double cda) {
        double avems = dist * 60 / minutes * 1000 / 3600;;
        double deg = Math.atan(drop / dist * 1000);
        double g = 9.806649999999999;//重力加速度
        double air_density = 352.989 / (273 + 22);//22の部分が温度
        double fd = (air_density * cda * avems * avems / 2) * avems;
        double fg = weight * g * Math.sin(deg) * avems;
        double frr = weight * g * 0.48 * avems;//0.48の部分が転がり抵抗。
        double w= fd + fg + frr;
        double watt = Math.round(w);
        double avesp = //出力に対して、空気抵抗及び転がり抵抗及び勾配の抵抗を引いた物。

    }*/



