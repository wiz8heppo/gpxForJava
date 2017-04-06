package com.gpx;

import java.util.ArrayList;
import java.util.List;

public class Segment {//2点間の距離と落差を表すクラスです。

    private double dist = 0d;//始点から終点までの距離(m)
    private double drop = 0d;//始点から終点までの落差。
    private double slope = 0;//斜度
    private int profile;//0は下り、1は平坦、2は緩い登 、　3はきつい登。//後にコーナー　４　を追加。
    private List<Point3D> points = new ArrayList<>();//このsegmentが内包する座標をすべて格納します。

    Segment(double dist, double drop) {
        this.dist = dist;
        this.drop = drop;
        slope = 100*drop/dist;
        profile = slope < -2 ? 0 : slope >= -2 && slope < 2 ? 1 : 2  ;//コーナーの判定式を追加

    }
    private Segment(double dist, double drop, List<Point3D> points) {
        this(dist, drop);
        this.points = points;

    }

    double getDist() {
        return dist;
    }

    double getDrop() {
        return drop;
    }

    double getSlope() {
        return slope;
    }

    int getProfile() {
        return profile;
    }

    public List<Point3D> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return
                "距離(m)=　" + String.format("%.2f", dist) +
                ", 高低差(m)=　" + String.format("%.2f", drop) +
                ", 斜度(%)=　" + String.format("%.2f", slope) +
                "}\n";
    }



    static List<Segment> createCource(List<Point3D> CP) {//受け取った三次元座標List<Point3D>から、隣り合う2点間をつないだ区間List<segment>を生成します。
        List<Segment> CPList = new ArrayList<>();
        for (int i = 0; i < CP.size() - 1; i++) {

            double dist = Coords.calcDistHubeny(CP.get(i).getLon(), CP.get(i).getLat(), CP.get(i + 1).getLon(), CP.get(i + 1).getLat());
            double drop =  CP.get(i + 1).getHeight() - CP.get(i).getHeight();
            double trueDist =Math.sqrt(dist * dist + drop * drop); //ヒュベニの公式は傾斜が考慮されていないので、再計算。
            List<Point3D> points = new ArrayList<>();
            points.add(CP.get(i));
            points.add(CP.get(i+1));
            CPList.add(new Segment(trueDist, drop, points));

        }
        return CPList;
    }

    private static List<Segment> concatFromDistance(List<Segment> SList, int distance) {//distance(m)未満のsegmentがある場合は、distanceより大きくなるまで隣の区間とに結合しつづけます。
        ArrayList<Segment> nSList = new ArrayList<>();

        for (int i = 0; i < SList.size(); i++) {
                double DS = SList.get(i).dist;
                double DR = SList.get(i).drop;
                List<Point3D> points = new ArrayList<>(SList.get(i).points);

                for(int j = i+1; j < SList.size() && DS < distance ; j++){
                    DS += SList.get(j).dist;
                    DR += SList.get(j).drop;
                    points.addAll(new ArrayList<>(SList.get(j).points.subList(1, SList.get(j).points.size())));
                    i = j;
                }
                if(i==SList.size()-1){
                    if(DS < distance){
                        DS += nSList.get(nSList.size()-1).dist;
                        DR += nSList.get(nSList.size()-1).drop;
                        points.addAll(0,new ArrayList<>(SList.get(nSList.size()-1).points.subList(1, SList.get(nSList.size()-1).points.size())));
                        nSList.remove(nSList.size()-1);
                    }
                }
                nSList.add(new Segment(DS, DR, points));

        }
        return nSList;

    }

    private static List<Segment> concatFromProfile(List<Segment> SList) {//隣り合うsegmentが同じprofileの場合、2つを結合します。たとえば区間が平坦ならば、次に上り坂か下り坂が現れるまでひとまとめのsegmentとしてインスタンス同士を結合します、
        List<Segment> nSList = new ArrayList<>();

        for (int i = 0; i < SList.size(); i++) {
            double DS = SList.get(i).dist;
            double DR = SList.get(i).drop;
            List<Point3D> points = new ArrayList<>(SList.get(i).points);

            for (int j = i + 1; i < SList.size()-1 && SList.get(i).profile == SList.get(j).profile ; j++) {
                    DS += SList.get(j).dist;
                    DR += SList.get(j).drop;
                    points.addAll(new ArrayList<>(SList.get(j).points.subList(1, SList.get(j).points.size())));

                i = j;
            }
            nSList.add(new Segment(DS, DR ,points));
        }
            return nSList;
    }
    static List<Segment> concatDefault(List<Segment> SList){
        List<Segment> nSList = Segment.concatFromProfile(SList);
        nSList = Segment.concatFromDistance(nSList,25);
        nSList = Segment.concatFromProfile(nSList);
        nSList = Segment.concatFromDistance(nSList,50);
        nSList = Segment.concatFromProfile(nSList);
        nSList = Segment.concatFromDistance(nSList,100);
        nSList = Segment.concatFromProfile(nSList);
        nSList = Segment.concatFromDistance(nSList,200);
        nSList = Segment.concatFromProfile(nSList);
        return nSList;
    }

}
