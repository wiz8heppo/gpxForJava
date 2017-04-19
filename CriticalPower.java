import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heppo on 2017/04/18.
 */
public class CriticalPower {
    int sec = 0;
    int power =0;


    private CriticalPower(int sec, int power){
            this.sec = sec;
            this.power = power;
    }


    public static List<CriticalPower> createPowerProfile(String adress){
        List<CriticalPower> p = new ArrayList<>();
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
            InputStreamReader isr = new InputStreamReader(http.getInputStream(), "EUC-JP");//GC起動中しかリードできない
            // 行単位で読み込む為の準備
            BufferedReader br = new BufferedReader(isr);
            String str;
            int sec;
            int power;
            System.out.println(br.readLine());
            while ((str = br.readLine()) != null) {
                    sec = Integer.parseInt(str.substring(0 , str.indexOf(",")));
                    power = (int) Double.parseDouble( str.substring(str.indexOf(",") + 2, str.length()));
                    if(sec >= 20000) break;

                System.out.println(sec);
                System.out.println(power);
                    p.add(new CriticalPower(sec , power));

            }
            br.close();
            isr.close();
            http.disconnect();
        }
        catch( Exception e ) {System.out.println("errot");

        }
        return p;
    }

    @Override
    public String toString() {
        return "CriticalPower{" +
                "sec=" + sec +
                ", power=" + power +
                '}';
    }
}