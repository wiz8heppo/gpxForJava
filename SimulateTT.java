package com.gpx;

import java.util.List;

class SimulateTT {
    private List<Segment> course;
    private HumanProfile rider;
    private double temperature;
    private double avp = 0;//平均出力（分）
    private double total_power = 0;
    private double time = 0;
    private double dist;


    SimulateTT(List<Segment> course, HumanProfile rider, double watt , double temperature) {
        this.course = course;
        this.rider = rider;
        double a, b, c, air_density, g, deg, cdaFromProfile;
        air_density = 352.989 / (273 + temperature);
        g = 9.806649999999999;
        for (Segment segment : course) {
            double minute = 0.01;
            double w;
            cdaFromProfile = rider.cda[segment.getProfile()];
            deg = Math.atan(segment.getDrop() / segment.getDist());

            for (double val = 10; ; minute += val) {
                double ave = segment.getDist() / 1000 * 60 / minute;
                double avms = ave * 1000 / 3600;
                a = (air_density * cdaFromProfile * avms * avms / 2) * avms;//空気抵抗の計算式
                b = rider.total_weight * g * Math.sin(deg) * avms;//登坂抵抗の計算式
                c = rider.total_weight * g * rider.roll_res * avms;//転がり抵抗の計算式
                w = a + b + c;

                if (w < (watt + 0.01) && w > (watt - 0.01)) {
                    break;
                }


                if (watt > w) {//精度が出るまで、変数の幅を狭めて計算を繰り返します。スマートにしたい。
                    minute -= val;
                    val /= 10;
                }
            }
            time += minute;
            dist += segment.getDist();
            System.out.println(segment+"現在の走行距離は"+String.format("%.2f",dist/1000) +"キロです\n");
        }
        System.out.println(toString(course)+"\n"+String.format("%.2f" , time)+"分");
        System.out.println(String.format("%.2f", dist / 1000)+"キロ");
        System.out.println("平均速度は"+String.format("%.2f", (dist / 1000)/(time/60))+"km/h");
    }
    SimulateTT(List<Segment> course, HumanProfile rider, double wattOfProfile[] , double temperature) {
        this.course = course;
        this.rider = rider;
        double a, b, c, air_density, g, deg, cdaFromProfile;
        air_density = 352.989 / (273 + temperature);
        g = 9.806649999999999;
        for (Segment segment : course) {
            double minute = 0.01;
            double w;
            cdaFromProfile = rider.cda[segment.getProfile()];
            deg = Math.atan(segment.getDrop() / segment.getDist());

            for (double val = 10; ; minute += val) {
                double ave = segment.getDist() / 1000 * 60 / minute;
                double avms = ave * 1000 / 3600;
                a = (air_density * cdaFromProfile * avms * avms / 2) * avms;//空気抵抗の計算式
                b = rider.total_weight * g * Math.sin(deg) * avms;//登坂抵抗の計算式
                c = rider.total_weight * g * rider.roll_res * avms;//転がり抵抗の計算式
                w = a + b + c;
                double watt = wattOfProfile[segment.getProfile()];

                if (w < (watt + 0.01) && w > (watt - 0.01)) {
                    break;
                }

                if (watt > w) {//精度が出るまで、変数の幅を狭めて計算を繰り返します。スマートにしたい。
                    minute -= val;
                    val /= 10;
                }
            }
            time += minute;
            dist += segment.getDist();
            total_power += minute * w;
            avp =  total_power/time;
            System.out.println(segment+"現在"+String.format("%.1f",dist/1000) +"キロ地点で、平均パワーは" + String.format("%.1f",avp) + "Wです。"
                    + "この区間のタイムとパワーは" + String.format("%.2f",minute) +"分" + String.format("%.1f",w) + "Wです" ) ;
        }
        System.out.println(toString(course)+"\n"+"リザルト\n"+String.format("%.2f" , time)+"分");
        System.out.println(String.format("%.1f", avp)+"W");
        System.out.println("平均速度は"+String.format("%.2f", (dist / 1000)/(time/60))+"km/h");
    }
    private static String toString(List<Segment> Slist) {
        int uphillcount = 0;
        double uphill = 0;
        double mostlonghill = 0;
        double mostlongslope = 0;
        int downhillcount = 0;
        double downhill = 0;
        int flatcount = 0;
        double flat = 0;
        for (Segment a : Slist) {

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
        return  "このコースの登りは" + uphillcount + "区間で、登り区間の総距離は" +String.format("%.2f" ,  uphill / 1000) + "kmです。\n"+
                "このコースの平坦は" + flatcount + "区間で、平坦区間の総距離は" + String.format("%.2f" , flat / 1000) + "kmです。\n" +
                "このコースの下りは" + downhillcount + "区間で、下り区間の総距離は" + String.format("%.2f" , downhill / 1000) + "kmです。\n" +
                "このコースは合計" + (uphillcount + flatcount + downhillcount) + "区間で総距離は" + String.format("%.2f" , (uphill + flat + downhill) / 1000) + "kmです。\n" +
                "このコースの最も長い登りは" + String.format("%.2f" , mostlonghill / 1000) + "kmで、平均斜度は" + String.format("%.2f" , mostlongslope) + "%です。\n";
    }






}




