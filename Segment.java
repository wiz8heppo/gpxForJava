package com.gpx;

import java.util.ArrayList;

import static java.lang.StrictMath.round;

/**
 * Created by heppo on 2017/03/22.
 */
public class Segment {//受け取ったCourseを、distごとに分割します。
    double dist = 0d;//距離(m)
    double drop = 0d;//始点から終点までの落差。
    double slope = 0;//斜度
    int profile;//0は下り、1は平坦、2は登 //後にコーナー　４　を追加。

    public Segment(double dist, double drop) {
        this.drop = drop;
        slope = 100*drop/dist;
        this.dist = Math.sqrt(dist*dist+drop*drop);//ヒュベニの公式は斜度が考慮されていないので、計算し直します。
        profile = slope < -2 ? 0 : slope >= -2 && slope < 2.5 ? 1 : 2;//コーナーの判定式を追加

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


    public static ArrayList<Segment> createCource(ArrayList<Point3D> CP) {
        ArrayList<Segment> CPList = new ArrayList<>();
        for (int i = 0; i < CP.size() - 1; i++) {

            double dist = Coords.calcDistHubeny(CP.get(i).lon, CP.get(i).lat, CP.get(i + 1).lon, CP.get(i + 1).lat);
            double drop =  CP.get(i + 1).height - CP.get(i).height;
            Segment a = new Segment(dist, drop);
            CPList.add(a);

        }
        return CPList;

    }
    public static ArrayList<Segment> concatFromDist(ArrayList<Segment> SL, int distance) {//distance(m)未満のsegmentがある場合は、distanceより大きくなるまで隣の区間とに結合しつづけます。
        ArrayList<Segment> NCPList = new ArrayList<>();

        for (int i = 0; i < SL.size() - 1; i++) {
            double DS = SL.get(i).dist;
            double DR = SL.get(i).drop;
            if(DS <distance){
                for(int j = i+1; DS < distance && j<SL.size(); j++){
                    DS += SL.get(j).dist;
                    DR += SL.get(j).drop;
                    i = j;
                }
                NCPList.add(new Segment(DS,DR));
            }
            else{NCPList.add(SL.get(i));}
        }
        return NCPList;
    }

    public static ArrayList<Segment> concatFromDist(ArrayList<Segment> SL) {//百メートル以下の区間がある場合は、100M以上になるまで隣の区間とに結合しつづけます。
        ArrayList<Segment> RCPL = new ArrayList<>();

        for (int i = 0; i < SL.size() - 1; i++) {
            double DS = SL.get(i).dist;
            double DR = SL.get(i).drop;
            if(DS <100d){
                for(int j = i+1; DS < 100d && j<SL.size(); j++){
                    DS += SL.get(j).dist;
                    DR += SL.get(j).drop;
                    i = j;
                }
                RCPL.add(new Segment(DS,DR));
            }
            else{RCPL.add(SL.get(i));}
        }
        return RCPL;
    }

    public static ArrayList<Segment> concatFromProfile(ArrayList<Segment> CPL) {//隣り合うsegmentが同じprofileの場合、それら結合します。たとえば区間が平坦ならば、次に上り坂か下り坂が現れるまでひとまとめのsegmentとしてインスタンス同士を結合します、
        ArrayList<Segment> RCPL = new ArrayList<>();
        double DS;
        double DR;
        for (int i = 0; i < CPL.size() - 1; i++) {
            DS = CPL.get(i).dist;
            DR = CPL.get(i).drop;
            for (int j = i + 1; CPL.get(i).profile == CPL.get(j).profile && j < CPL.size() -1 ; j++) {
                    DS += CPL.get(i + 1).dist;
                    DR += CPL.get(i + 1).drop;
                    i = j;
                if(j == CPL.size()-2){
                    DS += CPL.get(j + 1).dist;
                    DR += CPL.get(j + 1).drop;
                }

            }
            RCPL.add(new Segment(DS, DR));
        }
            return RCPL;
    }
    public static ArrayList<Segment> concatDefault(ArrayList<Segment> SL){
        SL = Segment.concatFromProfile(SL);
        SL = Segment.concatFromDist(SL,50);
        SL = Segment.concatFromProfile(SL);
        return SL;
    }
}
