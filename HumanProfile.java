package com.gpx;

/**
 * Created by heppo on 2017/03/22.
 */
class HumanProfile {//TT走者の基本情報を格納します
    public double human_weight = 0;
    public double bicycle_weight = 0;
    public double total_weight = 0;
    public double Roll_res = 0.0048;
    public double avp = 0;//目標平均出力（分）
    public double[][] PowerProfile = new double[2][7];
    public double[] cda = new double[3];//下りのcda //平坦のcda //登りのcda・・・2つに分けるか？

    public HumanProfile(double human_weight, double bicycle_weight, double avp ,double p60m, double p20m,
                        double p10m, double p5m, double p1m, double p30s, double pmax, double cda []) {
        this.human_weight = human_weight;
        this.bicycle_weight = bicycle_weight;
        total_weight = human_weight + bicycle_weight;

        PowerProfile[0][6] = p60m;
        PowerProfile[0][5] = p20m;
        PowerProfile[0][4] = p10m;
        PowerProfile[0][3] = p5m;
        PowerProfile[0][2] = p1m;
        PowerProfile[0][1] = p30s;
        PowerProfile[0][0] = pmax;

        PowerProfile[1][6] = p60m / total_weight;
        PowerProfile[1][5] = p20m / total_weight;
        PowerProfile[1][4] = p10m / total_weight;
        PowerProfile[1][3] = p5m / total_weight;
        PowerProfile[1][2] = p1m / total_weight;
        PowerProfile[1][1] = p30s / total_weight;
        PowerProfile[1][0] = pmax / total_weight;

        this.cda = cda;

        this.avp = avp;

    }

}
