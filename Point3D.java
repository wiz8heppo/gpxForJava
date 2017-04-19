import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Point3D {//読み込んだGPXファイルの各座標を格納するクラスです。このインスタンスを大量に格納したListを扱ってコースを作っていきます。
    private double lat;//緯度
    private double lon;//経度
    private double height;//海抜

    private Point3D(double lat, double lon, double height){
        this.lat = lat;
        this.lon = lon;
        this.height = height;
    }

    double getLat() {
        return lat;
    }

    double getLon() {
        return lon;
    }

    double getHeight() {
        return height;
    }

    static List<Point3D> createPointList(String adress) {

        List<Point3D> pointList = new ArrayList<>();
        if (adress.startsWith("http")) {
            try {
                // ターゲット
                URL url = new URL(adress);
                // 接続オブジェクト
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                // GET メソッド
                http.setRequestMethod("GET");
                // 接続
                http.connect();
                // EUC-JP でリーダーを作成
                InputStreamReader isr = new InputStreamReader(http.getInputStream(), "EUC-JP");
                // 行単位で読み込む為の準備
                BufferedReader br = new BufferedReader(isr);
                String str;
                while ((str = br.readLine()) != null) {
                    double lat;
                    double lon;
                    double height;
                    if (str.startsWith("<trkpt lat=\"")) {
                        //緯度をgpxから取得
                        lat = Double.parseDouble( str.substring(str.indexOf("=") + 2 , str.lastIndexOf(" ") - 1));
                        //経度をgpxから取得
                        lon = Double.parseDouble( str.substring(str.lastIndexOf("=") + 2, str.indexOf(">") - 1));
                        // 海抜が含まれる行に移動（暫定）
                        str = br.readLine();
                        //海抜をgpxから取得
                        height = Double.parseDouble(str.substring(str.indexOf(">") + 1, str.lastIndexOf("<")));

                        pointList.add(new Point3D(lat, lon, height));
                    }
                }
                br.close();
                isr.close();
                http.disconnect();
            }
            catch( Exception e ) {

            }
            return pointList;
        }

        else {


            try {
                File file = new File(adress);//ルートラボのGPX
                BufferedReader br = new BufferedReader(new FileReader(file));

                String str;
                while ((str = br.readLine()) != null) {
                    double lat;
                    double lon;
                    double height;
                    if (str.startsWith("<trkpt lat=\"")) {
                        lat = Double.parseDouble(str.substring(str.indexOf("=") + 2, str.lastIndexOf(" ") - 1));//緯度をgpxから取得
                        lon = Double.parseDouble(str.substring(str.lastIndexOf("=") + 2, str.indexOf(">") - 1));//経度をgpxから取得
                        str = br.readLine();// 海抜が含まれる行に移動（暫定）
                        height = Double.parseDouble(str.substring(str.indexOf(">") + 1, str.lastIndexOf("<")));//高度をgpxから取得
                        pointList.add(new Point3D(lat, lon, height));
                    }
                }

                br.close();

            } catch (FileNotFoundException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
            return pointList;
        }

    }

    @Override
    public String toString() {
        return "\nPoint3D{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", height=" + height +
                "}";
    }
}


