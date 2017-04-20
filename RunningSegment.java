public class RunningSegment extends Segment {
    private double time;//この区間の通過にかかった時間
    private double current_dist;//コース中の現在地点
    private double current_time;//この区間に到達するまでにかかった時間
    private double avp;//この区間に到達するまでの平均出力
    private double power;//この区間のみの平均出力

    private RunningSegment(Segment run, double current_dist, double time, double current_time) {
        super(run.getDist(), run.getDrop());
        this.time = 0.0D;
        this.current_dist = 0.0D;
        this.current_time = 0.0D;
        this.avp = 0.0D;
        this.power = 0.0D;
        this.current_dist = current_dist;
        this.time = time;
        this.current_time = current_time;
    }

    RunningSegment(Segment run, double current_dist, double time, double current_time, double avp, double power) {
        this(run, current_dist, current_time, time);
        this.avp = avp;
        this.power = power;
    }

    public double getTime() {
        return this.time;
    }

    public double getCurrent_dist() {
        return this.current_dist;
    }

    public double getCurrent_time() {
        return this.current_time;
    }

    public double getAvp() {
        return this.avp;
    }

    public double getPower() {
        return this.power;
    }

    public String toString() {
        return "RunningSegment{" + super.toString() + "現在" + String.format("%.1f", this.current_dist / 1000.0D) + "キロ地点で、" +
                "平均パワーは" + String.format("%.1f", this.avp) + "Wです。" +
                "この区間のタイムとパワーは" + String.format("%.2f", this.time) + "分" + String.format("%.1f", this.power) + "Wです\n";
    }
}
