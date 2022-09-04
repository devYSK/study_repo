package com.ys.stock.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : ysk
 */
public class ViewCounter3 {

    private AtomicInteger count = new AtomicInteger();

    public int view() {
        return count.getAndIncrement(); // 값을 먼저 리턴하고 후에 증가. count++
//        return count.incrementAndGet(); // 값을 먼저 증가하고 후에 리턴. ++count
    }

}
