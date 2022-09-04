package com.ys.stock.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : ysk
 */
public class ViewCounter2 {
    private int count = 0;
    private Lock lock = new ReentrantLock();

    public int view() {
        return count++;
    }

    public Lock getLock() {
        return lock;
    }

    public static void main(String[] args) {
        ViewCounter2 viewCounter = new ViewCounter2();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                viewCounter.getLock().lock();
                System.out.println(viewCounter.view());
                viewCounter.getLock().unlock();
            }).start();
        }
    }
}
