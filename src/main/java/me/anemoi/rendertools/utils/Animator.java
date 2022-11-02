package me.anemoi.rendertools.utils;

public class Animator {
    private final double duration;
    private long startTime;

    public Animator(double duration) {
        this.duration = duration;
    }

    public double getValue(double d, double d1, boolean increasing, boolean flag) {
        double value;
        double time = (double)(System.currentTimeMillis() - this.startTime) / this.duration;
        time = flag ? 2.0 * time * time : (time -= 1.0) * time * time + 1.0;
        double d2 = value = increasing ? d + time * (d1 - d) : d1 + time * (d - d1);
        if (increasing && d1 < value) {
            value = d1;
        } else if (!increasing && value < d) {
            value = d;
        }
        return value;
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
    }
}
