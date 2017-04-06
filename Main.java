package com.gpx;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;


public class Main {

    public static void main(String[] args) {
        out.println(" 斜度変化に富んだコースを状況ごとに分割して、TTや一人逃げの時に何処でパワーを出すと平均パワーに対して平均速度が上がるのかをシミュレーションしますぞ。\n" +
                "前面投影面積も斜度に応じて変更できるので、フォームの重要性も再認識できるはず！"+
                "\nただし、コースはすべて直線として作成されるためコーナーでの減速はいっさい考慮されてない故、下りの出力は控えめに！(今後直す予定です)"+
                "\n入力する数値はすべて半角で、単位を入れずに数字だけを打ち込んでください。" +
                "\n小数点以下を入力するときは、コンマ(,)ではなくドット(.)で区切ってください" +
                "\n\n まずはファイルの保存場所、もしくはインターネット上のアドレスを入力してください(ルートラボのGPXファイルのアドレスです)"+
                "\n例：https://latlonglab.yahoo.co.jp/route/get?id=014d652586365de02407a466b702f690&format=gpx\n" +
                        " ↑こんなGPXファイルアドレスが、ルートラボの画面下部「ルートのダウンロード」の項目にあります"
        +"\n\n この行の下にアドレスを入力。上のアドレスを入力すると、八方ヶ原が設定されますよ～");

        Scanner sc = new Scanner(System.in);
        String adress = sc.next();
        out.println("体重は？(kg)");
        double hw = sc.nextDouble();
        out.println("自転車を含めた装備の重量は？(kg)");
        double bw = sc.nextDouble();
       double power [] = new double[]{0,0,0,0,0,0,0};
        double wattOfProfile [] = new double[3];

        do {
            out.println("下りは何ワットで走りますか？（斜度 -2パーセント未満）");
            wattOfProfile[0] = sc.nextDouble();
            out.println("平坦は何ワットで走りますか？（斜度 -2～2パーセント）");
            wattOfProfile[1] = sc.nextDouble();
            out.println("登りは何ワットで走りますか？(斜度 2パーセント以上)");
            wattOfProfile[2] = sc.nextDouble();
            out.println("下り、平坦、登りの前面投影面積を順番に入力して（下ハンで約0.28,TTポジションで約0.24らしいですよ）");
            double cda[] = new double[3];
            out.println("下りのCdA");
            cda[0] = sc.nextDouble();
            out.println("平坦のCdA");
            cda[1] = sc.nextDouble();
            out.println("登りのCdA");
            cda[2] = sc.nextDouble();

            out.println("気温は？（摂氏）");
            double temp = sc.nextDouble();
            List<Point3D> P = Point3D.createPointList(adress);
            List<Segment> CP = Segment.concatDefault(Segment.createCource(P));
            HumanProfile you = new HumanProfile(hw, bw, power, cda);
            SimulateTT trial = new SimulateTT(CP, you, wattOfProfile, temp);
            out.println("コースと体重を変えずに再計算を行う場合は半角で true と入力してください。それ以外はこのソフトを再起動してください");
        }while (sc.nextBoolean());

        }
    }




