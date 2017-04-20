
import java.util.ArrayList;
import java.util.List;

public class Segment {
    private double dist;//距離
    private double drop;//落差
    private double slope;//斜度
    private int profile;//傾斜状況
    private List<Point3D> points;//この区間に含まれる座標

    Segment(double dist, double drop) {
        this.dist = 0.0D;
        this.drop = 0.0D;
        this.slope = 0.0D;
        this.points = new ArrayList<>();
        this.dist = dist;
        this.drop = drop;
        this.slope = 100.0D * drop / dist;
        this.profile = this.slope < -2.0D?0:(this.slope >= -2.0D && this.slope < 2.0D?1:2);
    }

    private Segment(double dist, double drop, List<Point3D> points) {
        this(dist, drop);
        this.points = points;
    }

    double getDist() {
        return this.dist;
    }

    double getDrop() {
        return this.drop;
    }

    double getSlope() {
        return this.slope;
    }

    int getProfile() {
        return this.profile;
    }

    public List<Point3D> getPoints() {
        return this.points;
    }

    public String toString() {
        return "距離(m)=　" + String.format("%.2f", this.dist) + ", 高低差(m)=　" + String.format("%.2f", this.drop) + ", 斜度(%)=　" + String.format("%.2f", this.slope) + "}\n";
    }

    static List<Segment> createCource(List<Point3D> CP) {
        ArrayList<Segment> CPList = new ArrayList<>();

        for(int i = 0; i < CP.size() - 1; ++i) {
            double dist = Coords.calcDistHubeny(CP.get(i).getLon(), CP.get(i).getLat(), CP.get(i + 1).getLon(), CP.get(i + 1).getLat());
            double drop = CP.get(i + 1).getHeight() - CP.get(i).getHeight();
            double trueDist = Math.sqrt(dist * dist + drop * drop);
            ArrayList<Point3D> points = new ArrayList<Point3D>();
            points.add(CP.get(i));
            points.add(CP.get(i + 1));
            CPList.add(new Segment(trueDist, drop, points));
        }

        return CPList;
    }

    private static List<Segment> concatFromDistance(List<Segment> SList, int distance) {
        ArrayList<Segment> nSList = new ArrayList<Segment>();

        for(int i = 0; i < SList.size(); ++i) {
            double DS = SList.get(i).dist;
            double DR = SList.get(i).drop;
            ArrayList<Point3D> points = new ArrayList<>(SList.get(i).points);

            for(int j = i + 1; j < SList.size() && DS < (double)distance; i = j++) {
                DS += SList.get(j).dist;
                DR += SList.get(j).drop;
                points.addAll(new ArrayList<>(SList.get(j).points.subList(1, SList.get(j).points.size())));
            }

            if(i == SList.size() - 1 && DS < (double)distance) {
                DS += (nSList.get(nSList.size() - 1)).dist;
                DR += (nSList.get(nSList.size() - 1)).drop;
                points.addAll(0, new ArrayList<>(SList.get(nSList.size() - 1).points.subList(1, SList.get(nSList.size() - 1).points.size())));
                nSList.remove(nSList.size() - 1);
            }

            nSList.add(new Segment(DS, DR, points));
        }

        return nSList;
    }

    private static List<Segment> concatFromProfile(List<Segment> SList) {
        ArrayList<Segment> nSList = new ArrayList<>();

        for(int i = 0; i < SList.size(); ++i) {
            double DS = SList.get(i).dist;
            double DR = SList.get(i).drop;
            ArrayList<Point3D> points = new ArrayList<>(SList.get(i).points);

            for(int j = i + 1; i < SList.size() - 1 && SList.get(i).profile == SList.get(j).profile; i = j++) {
                DS += SList.get(j).dist;
                DR += SList.get(j).drop;
                points.addAll(new ArrayList<>(SList.get(j).points.subList(1, SList.get(j).points.size())));
            }

            nSList.add(new Segment(DS, DR, points));
        }

        return nSList;
    }

    static List<Segment> concatDefault(List<Segment> SList) {
        List<Segment> nSList = concatFromProfile(SList);
        nSList = concatFromDistance(nSList, 25);
        nSList = concatFromProfile(nSList);
        nSList = concatFromDistance(nSList, 50);
        nSList = concatFromProfile(nSList);
        nSList = concatFromDistance(nSList, 100);
        nSList = concatFromProfile(nSList);
        nSList = concatFromDistance(nSList, 150);
        nSList = concatFromProfile(nSList);
        return nSList;
    }
}
