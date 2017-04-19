
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        List<CriticalPower> power = new ArrayList<>();
        System.out.println("斜度変化に富んだコースを状況ごとに分割して、TTや一人逃げの時に何処でパワーを出すと平均パワーに対して平均速度が上がるのかをシミュレーションしますぞ。\n" +
                "ただし、コースはすべて直線として作成されるためコーナーでの減速はいっさい考慮されてない故、下りの出力は控えめに！(今後直す予定です)\n" +
                "入力する数値はすべて半角で、単位を入れずに数字だけを打ち込んでください。\n" +
                "小数点以下を入力するときは、コンマ(,)ではなくドット(.)で区切ってください\n\n" +
                " まずはファイルの保存場所、もしくはインターネット上のアドレスを入力してください(ルートラボのGPXファイルのアドレスです)\n" +
                "例：https://latlonglab.yahoo.co.jp/route/get?id=014d652586365de02407a466b702f690&format=gpx\n" +
                " ↑こんなGPXファイルアドレスが、ルートラボの画面下部「ルートのダウンロード」の項目にあります\n\n" +
                " この行の下にアドレスを入力。上のアドレスを入力すると、八方ヶ原が設定されますよ～");
        Scanner sc = new Scanner(System.in);
        String adress = sc.next();
        System.out.println("体重は？(kg)");
        double hw = sc.nextDouble();
        System.out.println("自転車を含めた装備の重量は？(kg)");
        double bw = sc.nextDouble();

        System.out.println("気温は？（摂氏）");
        double temp = sc.nextDouble();
        System.out.println("少々お待ちを");
        List<Point3D> P = Point3D.createPointList(adress);
        List<Segment> CP = Segment.concatDefault(Segment.createCource(P));

        double[] watt = new double[3];
        do {
            System.out.println(
                    "何が知りたいですか？\n" +
                            "1.フォームによる空気抵抗(CdA)がタイムに与える影響\n" +
                            "2.パワーがタイムに与える影響\n" +
                            "3.コースをざっくりと上り下り平坦の三つに分けて、それぞれにパワーとフォーム(CdA)を設定してタイムを知りたい。\n" +
                            "4.つべこべ言わず一区間ずつパワーとフォーム(CdA)を設定していきたい" +
                            "\n5.その他。" +
                            "\n知りたい項目の番号を入力してください。");
            int select = sc.nextInt();
            double[] trial;
            HumanProfile you;
            SimulateTT[] trial1;
            if(select == 1) {
                System.out.println("パワーは固定のままフォーム(CdA)を３段階設定して、それぞれのタイムを出します");
                System.out.println("何ワットで走りますか？");
                watt[0] = sc.nextDouble();
                System.out.println("前面投影面積を入力してください（下ハンで約0.28,TTポジションで約0.24らしいですよ）");
                trial = new double[3];
                System.out.println("第1走のCdA");
                trial[0] = sc.nextDouble();
                System.out.println("第2走のCdA");
                trial[1] = sc.nextDouble();
                System.out.println("第3走のCdA");
                trial[2] = sc.nextDouble();
                you = new HumanProfile(hw, bw, power, trial);
                trial1 = new SimulateTT[3];

                for(int i = 0; i < trial1.length; ++i) {
                    trial1[i] = new SimulateTT(CP, you, watt[0], trial[i], temp);
                    System.out.println("CdA" + trial[i] + "の場合は" + String.format("%.2f", trial1[i].getTime()) + "分");
                }

            }

            else if (select ==2) {
                System.out.println("フォーム(CdA)は固定のままパワーを３段階設定して、それぞれのタイムを出します");
                System.out.println("第1走は平均何ワットで走りますか？");
                watt[0] = sc.nextDouble();
                System.out.println("第2走は平均何ワットで走りますか？");
                watt[1] = sc.nextDouble();
                System.out.println("第3走は平均何ワットで走りますか？");
                watt[2] = sc.nextDouble();
                System.out.println("前面投影面積を入力してください（下ハンで約0.28,TTポジションで約0.24らしいですよ）");
                trial = new double[]{sc.nextDouble(), 0.0D, 0.0D};
                you = new HumanProfile(hw, bw, power, trial);
                trial1 = new SimulateTT[3];

                for(int i = 0; i < trial1.length; ++i) {
                    trial1[i] = new SimulateTT(CP, you, watt[i], trial[0], temp);
                    System.out.println("CdA" + watt[i] + "の場合は" + String.format("%.2f", trial1[i].getTime()) + "分");
                }

            }

           else if(select == 3) {
                System.out.println("下りは何ワットで走りますか？（斜度 -2パーセント未満）");
                watt[0] = sc.nextDouble();
                System.out.println("平坦は何ワットで走りますか？（斜度 -2～2パーセント）");
                watt[1] = sc.nextDouble();
                System.out.println("登りは何ワットで走りますか？(斜度 2パーセント以上)");
                watt[2] = sc.nextDouble();
                System.out.println("下り、平坦、登りのフォーム(CdA)を順番に入力して（下ハンで約0.28,TTポジションで約0.24らしいですよ）");
                trial = new double[3];
                System.out.println("下りのCdA");
                trial[0] = sc.nextDouble();
                System.out.println("平坦のCdA");
                trial[1] = sc.nextDouble();
                System.out.println("登りのCdA");
                trial[2] = sc.nextDouble();
                you = new HumanProfile(hw, bw, power, trial);
                SimulateTT run = new SimulateTT(CP, you, watt, temp);
                System.out.print(run.toString());
                System.out.println("詳細を表示しますか？YES = 1 NO = 0 と入力");
                if(sc.nextInt() == 1) {
                    for(Segment b : run.getRunning_course()) {
                        System.out.println(b.toString());
                    }
                }
            }

            else if(select == 4) {
                System.out.println("斜度の移り変わりによって区間を分けます。");
                SimulateTT run = new SimulateTT(CP, hw + bw, temp);
                System.out.print(run.toString());
            }
            else {
                System.out.println("要相談！ゴールデンチーターのプロファイルを取得して、最適な出力を計算します\n" +
                        "ゴールデンチーターを起動してから、下にゴールデンチーターのアスリート名を入れてください");
                String ath = sc.next();
                power = CriticalPower.createPowerProfile("http://localhost:12021/"+ath+"/meanmax/bests");
            }

            System.out.println("コースと体重を変えずに再計算を行う場合は半角で true と入力してください。それ以外はこのソフトを再起動してください");
        } while(sc.nextBoolean());

    }
}
