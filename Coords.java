package com.gpx;

/**
 * Created by heppo on 2017/03/23.
 */
public class Coords {


    public static final double WGS84_A = 6378137.000;
    public static final double WGS84_E2 = 0.00669437999019758;
    public static final double WGS84_MNUM = 6335439.32729246;


    public static double deg2rad(double deg) {
        return deg * Math.PI / 180.0;
    }

    public static double calcDistHubeny(double lat1, double lng1,
                                        double lat2, double lng2,
                                        double a, double e2, double mnum) {
        double my = deg2rad((lat1 + lat2) / 2.0);
        double dy = deg2rad(lat1 - lat2);
        double dx = deg2rad(lng1 - lng2);

        double sin = Math.sin(my);
        double w = Math.sqrt(1.0 - e2 * sin * sin);
        double m = mnum / (w * w * w);
        double n = a / w;

        double dym = dy * m;
        double dxncos = dx * n * Math.cos(my);

        return Math.sqrt(dym * dym + dxncos * dxncos);
    }

    public static double calcDistHubeny(double lat1, double lng1,
                                        double lat2, double lng2) {
        return calcDistHubeny(lat1, lng1, lat2, lng2,
                WGS84_A, WGS84_E2, WGS84_MNUM);
    }
}