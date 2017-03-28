package com.gpx;

import java.util.ArrayList;
import java.util.List;

public class Segment {//受け取ったCourseを、distごとに分割します。
    private double dist = 0d;//距離(m)
    private double drop = 0d;//始点から終点までの落差。
    private double slope = 0;//斜度
    private int profile;//0は下り、1は平坦、2は緩い登 、　3はきつい登。//後にコーナー　４　を追加。
    private List<Point3D> points = new ArrayList<>();//このsegmentが内包する座標をすべて格納します。

    private Segment(double dist, double drop) {
        this.dist = dist;
        this.drop = drop;
        slope = 100*drop/dist;
        profile = slope < -2 ? 0 : slope >= -2 && slope < 1.5 ? 1 : slope >= 1.5  && slope < 5 ? 2 : 3 ;//コーナーの判定式を追加

    }
    private Segment(double dist, double drop, List<Point3D> points) {
        this(dist, drop);
        this.points = points;

    }

    @Override
    public String toString() {
        return "Segment{" +
                "dist=" + dist +
                ", drop=" + drop +
                ", slope=" + slope +
                ", profile=" + profile +
                '}';
    }



    public static List<Segment> createCource(List<Point3D> CP) {
        List<Segment> CPList = new ArrayList<>();
        for (int i = 0; i < CP.size() - 1; i++) {

            double dist = Coords.calcDistHubeny(CP.get(i).getLon(), CP.get(i).getLat(), CP.get(i + 1).getLon(), CP.get(i + 1).getLat());
            double drop =  CP.get(i + 1).getHeight() - CP.get(i).getHeight();
            double trueDist =Math.sqrt(dist * dist + drop * drop);
            List<Point3D> points = new ArrayList<>();
            points.add(CP.get(i));
            points.add(CP.get(i+1));
            CPList.add(new Segment(trueDist, drop, points));

        }
        return CPList;
    }

    public static ArrayList<Segment> concatFromDist(List<Segment> SL, int distance) {//distance(m)未満のsegmentがある場合は、distanceより大きくなるまで隣の区間とに結合しつづけます。
        ArrayList<Segment> NCPList = new ArrayList<>();

        for (int i = 0; i < SL.size(); i++) {
                double DS = SL.get(i).dist;
                double DR = SL.get(i).drop;
                List<Point3D> points = new ArrayList<>(SL.get(i).points);

                for(int j = i+1; j < SL.size() && DS < distance ; j++){
                    DS += SL.get(j).dist;
                    DR += SL.get(j).drop;
                    points.addAll(new ArrayList<>(SL.get(j).points.subList(1, SL.get(j).points.size())));
                    i = j;
                }
                NCPList.add(new Segment(DS, DR, points));

        }
        return NCPList;
    }

    public static List<Segment> concatFromProfile(List<Segment> SL) {//隣り合うsegmentが同じprofileの場合、それら結合します。たとえば区間が平坦ならば、次に上り坂か下り坂が現れるまでひとまとめのsegmentとしてインスタンス同士を結合します、
        List<Segment> NCPList = new ArrayList<>();

        for (int i = 0; i < SL.size(); i++) {
            double DS = SL.get(i).dist;
            double DR = SL.get(i).drop;
            List<Point3D> points = new ArrayList<>(SL.get(i).points);

            for (int j = i + 1; j < SL.size() - 2 && SL.get(j-1).profile == SL.get(j).profile ; j++) {
                    DS += SL.get(j).dist;//ここ、なんでSL.get(i+1).distだったの？？おぼえてねえ
                    DR += SL.get(j).drop;//ここ、なんでSL.get(i+1).dropだったの？？おぼえてねえ
                    points.addAll(new ArrayList<>(SL.get(j).points.subList(1, SL.get(j).points.size())));

                i = j;
            }
            NCPList.add(new Segment(DS, DR ,points));
        }
            return NCPList;
    }
    public static List<Segment> concatDefault(List<Segment> SL){
        SL = Segment.concatFromProfile(SL);
        SL = Segment.concatFromDist(SL,100);
        SL = Segment.concatFromProfile(SL);
        return SL;
    }
}
