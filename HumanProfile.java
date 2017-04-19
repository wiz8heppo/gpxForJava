
import java.util.List;

class HumanProfile {
    double human_weight = 0.0D;
    double bicycle_weight = 0.0D;
    double total_weight = 0.0D;
    double roll_res = 0.0048D;
    List<CriticalPower> power;

    double[] cda = new double[3];

    HumanProfile(double human_weight, double bicycle_weight, List<CriticalPower> power, double[] cda) {
        this.human_weight = human_weight;
        this.bicycle_weight = bicycle_weight;
        this.total_weight = human_weight + bicycle_weight;
        this.cda = cda;
        this.power = power;

    }

    HumanProfile() {
    }


    }


