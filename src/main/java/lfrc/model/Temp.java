package lfrc.model;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Temp {

    // enabling fairness depends on the business requirements and the amount of readings arriving
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    // int and float would do it for this particular scenario
    private long count = 0;
    private double total = 0.0;

    public void addReading(double temperature) {
        lock.writeLock().lock();
        try {
            // consider throwing an exception when count or total overflows
            // very unlikely in this particular scenario, where we have only one temperature reading per day
            count++;
            total += temperature;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public double average() {
        lock.readLock().lock();
        try {
            return total / count;
        } finally {
            lock.readLock().unlock();
        }
    }
}
