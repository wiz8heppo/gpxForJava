package com.gpx;

import java.io.*;
import java.util.ArrayList;

public class Point3D {//読み込んだGPXファイルの各座標を格納するクラスです。このインスタンスを大量に格納したListを扱ってコースを作っていきます。
    double lat;//緯度
    double lon;//経度
    double height;//海抜

    private Point3D(double lat, double lon, double height){
        this.lat = lat;
        this.lon = lon;
        this.height = height;
    }


    public static ArrayList<Point3D> createPointList(String adress){

        ArrayList<Point3D> pointList = new ArrayList<>();

        try{
            File file = new File(adress);//ルートラボのGPX
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str;
            while((str = br.readLine()) != null){
                double lat;
                double lon;
                double height;
                if(str.startsWith("<trkpt lat=\"")){
                    lat = Double.parseDouble(str.substring(str.indexOf("=")+2, str.lastIndexOf(" ")-1));//緯度をgpxから取得
                    lon = Double.parseDouble(str.substring(str.lastIndexOf("=")+2, str.indexOf(">")-1));//経度をgpxから取得
                    str = br.readLine();
                    height = Double.parseDouble(str.substring(str.indexOf(">")+1, str.lastIndexOf("<")));//高度をgpxから取得
                    pointList.add(new Point3D(lat, lon, height));
                }
            }

            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        return pointList;
}
}


