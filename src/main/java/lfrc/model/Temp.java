package lfrc.model;

public class Temp {
    private long count = 0;
    private double total = 0.0;

    public synchronized void addReading(double temperature) {
        count++;
        total += temperature;
    }

    public synchronized double average() {
        return total / count;
    }
}
