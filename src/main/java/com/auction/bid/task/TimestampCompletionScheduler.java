package com.auction.bid.task;

import java.util.concurrent.PriorityBlockingQueue;

public class TimestampCompletionScheduler<E extends TimestampData> {
    private final PriorityBlockingQueue<E> timestampQueue = new PriorityBlockingQueue<>();

    public void runScheduler() {
        while (true) {
            try {
                E first = timestampQueue.take();
                if (first.getTimestamp() <= System.currentTimeMillis()) {
                    // TODO : 바로 입찰 완료 실행
                } else {
                    waitFirstItemTimestamp(first.getTimestamp());
                    // TODO : 대기 후 입찰 완료 실행
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void waitFirstItemTimestamp(long timestamp) throws InterruptedException {
        long gap = timestamp - System.currentTimeMillis();
        if (gap > 0) {
            Thread.sleep(gap);
        }
    }
}