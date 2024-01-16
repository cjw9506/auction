package com.auction.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 입찰 종료 알림 테스트
 */
class TimestampCompletionScheduler {
    private PriorityBlockingQueue<Long> timestampQueue = new PriorityBlockingQueue<>();

    @BeforeEach
    void beforeEach() {
        // 현재 시간보다 앞, 대기하지 않고 바로 출력
        timestampQueue.add(System.currentTimeMillis() - 1000);
        
        // 현재 시간보다 뒤, 대기 후 출력
        timestampQueue.add(System.currentTimeMillis() + 3000);
        timestampQueue.add(System.currentTimeMillis() + 5000);
        timestampQueue.add(System.currentTimeMillis() + 9000);
    }

    @Test
    @DisplayName("monitor bid completion time")
    void schedulerTest() {
        long startTime = System.currentTimeMillis();
        while (true) {
            try {
                Long first = timestampQueue.take();
                if (first <= System.currentTimeMillis()) {
                    System.out.println("first = " + first);
                } else {
                    System.out.println("sleep = " + (first - System.currentTimeMillis()));
                    Thread.sleep(first - System.currentTimeMillis());
                    System.out.println("first = " + first);
                }
                System.out.println("done");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 테스트문 종료를 위함. 실제로는 계속 돌아가도록
            if (timestampQueue.size() == 0) {
                break;
            }
        }
        long total = System.currentTimeMillis() - startTime;

        Assertions.assertAll(
                () -> Assertions.assertTrue(9000 >= total, "total time should be less than or equal to 9 seconds."),
                () -> Assertions.assertEquals(0, timestampQueue.size(), "size should be 0.")
        );
    }
}