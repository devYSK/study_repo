package com.ys.stock.concurrency;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : ysk
 */
@Slf4j
public class ViewCounter {

    private int count = 0;

    private Integer count2 = 0; // 변수는 객체여야 한다.

    public synchronized void view() { // 메서드 Lock
        this.count += 1;
        System.out.println("currentThreadName : " + Thread.currentThread().getName()
                + ", view Count : " + this.getViewCount());
    }

    public void view2() { // 구간, 변수 Lock
        synchronized (this.count2) { // 변수는 객체여야 한다.
            this.count2 += 1;
            System.out.println("currentThreadName : " + Thread.currentThread().getName()
                    + ", view Count : " + this.getViewCount());

        }
    }

    public int getViewCount() {
        return this.count;
    }

    public int getViewCount2() {
        return this.count2;
    }

    public static void main(String[] args) {
        int threadCount = 100;

        ViewCounter viewCounter = new ViewCounter();

        for (int i = 0; i < threadCount; i++) {
            new Thread(viewCounter::view).start();
        }

        System.out.println(viewCounter.getViewCount());
    }

}
