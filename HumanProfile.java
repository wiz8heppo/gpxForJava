package com.gpx;

class HumanProfile {//TT走者の基本情報を格納します
    double human_weight = 0;
    double bicycle_weight = 0;
    double total_weight = 0;
    double roll_res = 0.0048;//転がり抵抗
    double[] PowerProfile = new double[7];
    double[] cda = new double[3];//前面投影面積　下りのcda //平坦のcda //登りのcda・・・2つに分けるか？

    HumanProfile(double human_weight, double bicycle_weight, double power[], double cda[]) {
        this.human_weight = human_weight;
        this.bicycle_weight = bicycle_weight;
        total_weight = human_weight + bicycle_weight;

        PowerProfile = power;

        this.cda = cda;

    }
    HumanProfile() {}

}
