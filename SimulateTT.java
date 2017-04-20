
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class SimulateTT {
    private List<RunningSegment> running_course = new ArrayList<>();
    private HumanProfile rider = new HumanProfile();
    private double temperature;
    private double avp = 0.0D;
    private double total_power = 0.0D;
    private double time = 0.0D;
    private double dist;

    SimulateTT(List<Segment> course, HumanProfile rider, double[] wattOfProfile, double temperature) {//下り、平坦、登りごとにCdAとWを設定してタイムを算出するコンストラクタです。
        this.rider = rider;
        double air_density = 352.989D / (273.0D + temperature);
        final double g = 9.80665D;

        for (Segment segment : course) {
            double minute = 0.01D;
            double cdaFromProfile = rider.cda[segment.getProfile()];
            double deg = Math.atan(segment.getDrop() / segment.getDist());
            double val = 10.0D;

            while (true) {
                double ave = segment.getDist() / 1000.0D * 60.0D / minute;
                double avms = ave * 1000.0D / 3600.0D;
                double a = air_density * cdaFromProfile * avms * avms / 2.0D * avms;
                double b = rider.total_weight * g * Math.sin(deg) * avms;
                double c = rider.total_weight * g * rider.roll_res * avms;
                double w = a + b + c;
                double watt = wattOfProfile[segment.getProfile()];
                if (w < watt + 0.01D && w > watt - 0.01D) {
                    this.time += minute;
                    this.dist += segment.getDist();
                    this.total_power += minute * w;
                    this.avp = this.total_power / this.time;
                    this.running_course.add(new RunningSegment(segment, this.dist, minute, this.time, this.avp, w));
                    break;
                }

                if (watt > w) {
                    minute -= val;
                    val /= 10.0D;
                }

                minute += val;
            }
        }

    }

    SimulateTT(List<Segment> course, HumanProfile rider, double watt, double cda, double temperature) {//常に一定のCdA、一定のWで走り続けた場合のタイムを算出するコンストラクタです。
        this.rider = rider;
        double air_density = 352.989D / (273.0D + temperature);
        final double g = 9.80665D;

        for (Segment segment : course) {
            double minute = 0.01D;
            double deg = Math.atan(segment.getDrop() / segment.getDist());
            double val = 10.0D;

            while (true) {
                double ave = segment.getDist() / 1000.0D * 60.0D / minute;
                double avms = ave * 1000.0D / 3600.0D;
                double a = air_density * cda * avms * avms / 2.0D * avms;
                double b = rider.total_weight * g * Math.sin(deg) * avms;
                double c = rider.total_weight * g * rider.roll_res * avms;
                double w = a + b + c;
                if (w < watt + 0.01D && w > watt - 0.01D) {
                    this.time += minute;
                    this.dist += segment.getDist();
                    this.total_power += minute * w;
                    this.avp = this.total_power / this.time;
                    this.running_course.add(new RunningSegment(segment, this.dist, minute, this.time, this.avp, w));
                    break;
                }

                if (watt > w) {
                    minute -= val;
                    val /= 10.0D;
                }

                minute += val;
            }
        }

    }

    SimulateTT(List<Segment> course, HumanProfile rider, double temperature) {


    }

    SimulateTT(List<Segment> course, double total_weight, double temperature) {//区間ごとに毎回CdAとWを設定してタイムを算出するコンストラクタです
        Scanner sc = new Scanner(System.in);
        double air_density = 352.989D / (273.0D + temperature);
        double g = 9.80665D;

        for (Segment seg : course) {
            double minute = 0.01D;
            System.out.println(seg.toString());
            double deg = Math.atan(seg.getDrop() / seg.getDist());
            System.out.println("この区間は何ワットで走りますか？");
            double watt = sc.nextDouble();
            System.out.println("前面投影面積を入力してください（下ハンで約0.28,TTポジションで約0.24らしいですよ）");
            double cda = sc.nextDouble();
            double val = 10.0D;

            while (true) {
                double ave = seg.getDist() / 1000.0D * 60.0D / minute;
                double avms = ave * 1000.0D / 3600.0D;
                double a = air_density * cda * avms * avms / 2.0D * avms;
                double b = total_weight * g * Math.sin(deg) * avms;
                double c = total_weight * g * this.rider.roll_res * avms;
                double w = a + b + c;
                if (w < watt + 0.01D && w > watt - 0.01D) {
                    this.time += minute;
                    this.dist += seg.getDist();
                    this.total_power += minute * w;
                    this.avp = this.total_power / this.time;
                    this.running_course.add(new RunningSegment(seg, this.dist, minute, this.time, this.avp, w));
                    break;
                }

                if (watt > w) {
                    minute -= val;
                    val /= 10.0D;
                }

                minute += val;
            }
        }

    }

    public List<RunningSegment> getRunning_course() {
        return this.running_course;
    }

    public HumanProfile getRider() {
        return this.rider;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public double getAvp() {
        return this.avp;
    }

    public double getTotal_power() {
        return this.total_power;
    }

    public double getTime() {
        return this.time;
    }

    public double getDist() {
        return this.dist;
    }

    public String toString() {
        int uphillcount = 0;
        double uphill = 0.0D;
        double mostlonghill = 0.0D;
        double mostlongslope = 0.0D;
        int downhillcount = 0;
        double downhill = 0.0D;
        int flatcount = 0;
        double flat = 0.0D;

        for (RunningSegment seg : this.running_course) {
            if (seg.getProfile() > 1) {
                ++uphillcount;
                uphill += seg.getDist();
                if (seg.getDist() > mostlonghill) {
                    mostlonghill = seg.getDist();
                    mostlongslope = seg.getSlope();
                }
            }

            if (seg.getProfile() == 0) {
                ++downhillcount;
                downhill += seg.getDist();
            }

            if (seg.getProfile() == 1) {
                ++flatcount;
                flat += seg.getDist();
            }
        }

        return "リザルト\n合計タイム" + String.format("%.2f", this.time) + "分　　" +
                "平均パワー" + String.format("%.1f", this.avp) + "W　　" +
                "平均速度は" + String.format("%.2f", this.dist / 1000.0D / (this.time / 60.0D)) + "km/h\n" +
                "このコースの登りは" + uphillcount + "区間で、登り区間の総距離は" + String.format("%.2f", uphill / 1000.0D) + "kmです。\n" +
                "このコースの平坦は" + flatcount + "区間で、平坦区間の総距離は" + String.format("%.2f", flat / 1000.0D) + "kmです。\n" +
                "このコースの下りは" + downhillcount + "区間で、下り区間の総距離は" + String.format("%.2f", downhill / 1000.0D) + "kmです。\n" +
                "このコースは合計" + (uphillcount + flatcount + downhillcount) + "区間で総距離は" + String.format("%.2f", (uphill + flat + downhill) / 1000.0D) + "kmです。\n" +
                "このコースの最も長い登りは" + String.format("%.2f", mostlonghill / 1000.0D) + "kmで、平均斜度は" + String.format("%.2f", mostlongslope) + "%です。\n";
    }
}
