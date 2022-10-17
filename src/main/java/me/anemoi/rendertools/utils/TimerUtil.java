package me.anemoi.rendertools.utils;

public class TimerUtil {
    long lastReset;

    public TimerUtil() {
        reset();
    }

    public void reset() {
        lastReset = System.currentTimeMillis();
    }

    public boolean hasExpired(long timeout) {
        return System.currentTimeMillis() - lastReset > timeout;
    }
}
