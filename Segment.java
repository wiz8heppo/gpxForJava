package com.gpx;

import java.util.ArrayList;
import java.util.List;

public class Segment {//2点間の距離と落差を表すクラスです。

    private double dist = 0d;//始点から終点までの距離(m)
    private double drop = 0d;//始点から終点までの落差。
    private double slope = 0;//斜度
    private int profile;//0は下り、1は平坦、2は緩い登 、　3はきつい登。//後にコーナー　４　を追加。
    private List<Point3D> points = new ArrayList<>();//このsegmentが内包する座標をすべて格納します。

    private Segment(double dist, double drop) {
        this.dist = dist;
        this.drop = drop;
        slope = 100*drop/dist;
        profile = slope < -2 ? 0 : slope >= -2 && slope < 1 ? 1 : slope >= 1  && slope < 5 ? 2 : 3 ;//コーナーの判定式を追加

    }
    private Segment(double dist, double drop, List<Point3D> points) {
        this(dist, drop);
        this.points = points;

    }

    public double getDist() {
        return dist;
    }

    public double getDrop() {
        return drop;
    }

    public double getSlope() {
        return slope;
    }

    public int getProfile() {
        return profile;
    }

    public List<Point3D> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Segment{　" +
                "距離(m)=　" + dist +
                ", 高低差(m)=　" + drop +
                ", 斜度(%)=　" + slope +
                ", この区間の辛さ(0～3)=　" + profile +
                "}\n";
    }



    public static List<Segment> createCource(List<Point3D> CP) {//受け取った三次元座標List<Point3D>から、隣り合う2点間をつないだ区間List<segment>を生成します。
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

    public static ArrayList<Segment> concatFromDistance(List<Segment>SList, int distance) {//distance(m)未満のsegmentがある場合は、distanceより大きくなるまで隣の区間とに結合しつづけます。
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
                nSList.add(new Segment(DS, DR, points));

        }
        return nSList;

    }

    public static List<Segment> concatFromProfile(List<Segment> SList) {//隣り合うsegmentが同じprofileの場合、2つを結合します。たとえば区間が平坦ならば、次に上り坂か下り坂が現れるまでひとまとめのsegmentとしてインスタンス同士を結合します、
        List<Segment> nSList = new ArrayList<>();

        for (int i = 0; i < SList.size(); i++) {
            double DS = SList.get(i).dist;
            double DR = SList.get(i).drop;
            List<Point3D> points = new ArrayList<>(SList.get(i).points);

            for (int j = i + 1; j < SList.size() - 2 && SList.get(j-1).profile == SList.get(j).profile ; j++) {
                    DS += SList.get(j).dist;
                    DR += SList.get(j).drop;
                    points.addAll(new ArrayList<>(SList.get(j).points.subList(1, SList.get(j).points.size())));

                i = j;
            }
            nSList.add(new Segment(DS, DR ,points));
        }
            return nSList;
    }
    public static List<Segment> concatDefault(List<Segment> SList){
        List<Segment> nSList = Segment.concatFromProfile(SList);
        nSList = Segment.concatFromDistance(nSList,100);
        nSList = Segment.concatFromProfile(nSList);
        return nSList;
    }
    public  static String toString(List<Segment> Slist) {
        int uphillcount = 0;
        double uphill = 0;
        double mostlonghill = 0;
        double mostlongslope = 0;
        int downhillcount = 0;
        double downhill = 0;
        int flatcount = 0;
        double flat = 0;
        StringBuilder Seg = new StringBuilder();
        for (Segment a : Slist) {
            Seg.append(a.toString());

            if (a.getProfile() > 1) {
                uphillcount++;
                uphill += a.getDist();
                if (a.getDist() > mostlonghill) {
                    mostlonghill = a.getDist();
                    mostlongslope = a.getSlope();
                }
            }

            if (a.getProfile() == 0) {
                downhillcount++;
                downhill += a.getDist();
            }

            if (a.getProfile() == 1) {
                flatcount++;
                flat += a.getDist();
            }
        }
       return Seg.toString() + "このコースの登りは" + uphillcount + "区間で、登り区間の総距離は" + uphill / 1000 + "kmです。\n"+
        "このコースの平坦は" + flatcount + "区間で、平坦区間の総距離は" + flat / 1000 + "kmです。\n" +
        "このコースの下りは" + downhillcount + "区間で、下り区間の総距離は" + downhill / 1000 + "kmです。\n" +
        "このコースは合計" + (uphillcount + flatcount + downhillcount) + "区間で総距離は" + (uphill + flat + downhill) / 1000 + "kmです。\n" +
        "このコースの最も長い登りは" + mostlonghill / 1000 + "kmで、平均斜度は" + mostlongslope + "%です。\n";
    }
}
