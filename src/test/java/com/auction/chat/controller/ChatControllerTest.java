package com.auction.chat.controller;

import com.auction.auth.domain.User;
import com.auction.auth.repository.UserRepository;
import com.auction.chat.dto.MessageDTO;
import com.auction.chat.repository.ChatRepository;
import com.auction.room.domain.AuctionRoom;
import com.auction.room.repository.AuctionRoomRepository;
import com.auction.room.service.AuctionRoomService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatControllerTest {

    @Autowired
    private AuctionRoomService roomService;
    @Autowired
    private ChatController chatController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionRoomRepository roomRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ChatRepository chatRepository;


    static AtomicInteger num = new AtomicInteger(20000);

    @BeforeAll
    void before() {
        User user = new User("cjw9500", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user1 = new User("cjw9501", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user2 = new User("cjw9502", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user3 = new User("cjw9503", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user4 = new User("cjw9504", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user5 = new User("cjw9505", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user6 = new User("cjw9506", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user7 = new User("cjw9507", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user8 = new User("cjw9508", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        User user9 = new User("cjw9509", "$2a$10$dtdjo7OkPDBketSr.Vc8n.w3BdflhT4kpf.fvFDLpetsu3LXNGy36");
        userRepository.saveAll(List.of(user, user1, user2, user3, user4, user5, user6, user7, user8, user9));
        //User user = userRepository.findByAccount("cjw9506").orElseThrow(() -> new IllegalArgumentException("123123"));

        AuctionRoom room = AuctionRoom.builder()
                .uuid("e7361fb2-e4fe-4d26-b775-54e0dd0e3db2")
                .owner(user)
                .itemName("test")
                .highestBidUser(null)
                .startPrice(10000.0)
                .endPrice(10000.0)
                .startTimestamp(1000000000000L)
                .endTimestamp(2000000000000L)
                .build();

        redisTemplate.opsForHash().putAll(room.toInfoKeyString("e7361fb2-e4fe-4d26-b775-54e0dd0e3db2"), room.toValueMap());

        roomRepository.save(room);
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setSessionAttributes(new HashMap<>());
        MessageDTO message = MessageDTO.builder()
                .sender("cjw9506")
                .roomId("e7361fb2-e4fe-4d26-b775-54e0dd0e3db2")
                .build();
        chatController.addUser(message, "e7361fb2-e4fe-4d26-b775-54e0dd0e3db2", headerAccessor);
    }
    //Todo 유저를 실제로 만들어서 마지막에 최고가를 갱신한 금액과 유저가 맞는지 확인해야함

    @Test
    public void test() throws InterruptedException {
        String[] userList = {"cjw9500", "cjw9501", "cjw9502", "cjw9503", "cjw9504", "cjw9505", "cjw9506", "cjw9507", "cjw9508", "cjw9509"};
        int users = 10;
        int requestPerSecond = 50;
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < users; i++) {
            final int userIdx = i;
            executor.execute(() -> {
                for (int j = 0; j < requestPerSecond; j++) {
                    MessageDTO message = new MessageDTO();
                    message.setContent((double) num.getAndIncrement());
                    message.setSender(userList[userIdx]);
                    String roomId = "e7361fb2-e4fe-4d26-b775-54e0dd0e3db2";
                    message.setRoomId(roomId);
                    chatController.sendMessage(message, roomId);
                }
            });
        }
        Thread.sleep(20000);
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 종료 시간 측정
        long endTime = System.currentTimeMillis();

        // 소요 시간 계산
        long durationMilliseconds = endTime - startTime;
        double durationSeconds = durationMilliseconds / 1000.0;
        String key = "auction:room:" + "e7361fb2-e4fe-4d26-b775-54e0dd0e3db2" + ":info";
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        Double currentEndPrice = Optional.ofNullable((String) entries.get("endPrice"))
                .map(Double::valueOf)
                .orElse(null);
        String highestBidUserId = Optional.ofNullable((String) entries.get("highestBidUserId"))
                .orElse(null);
        Optional<AuctionRoom> room = roomRepository.findById("e7361fb2-e4fe-4d26-b775-54e0dd0e3db2");

        Assertions.assertEquals(room.get().getHighestBidUser().getId().toString(), highestBidUserId);
        Assertions.assertEquals(currentEndPrice, num.get() - 1);
        System.out.println("총 소요 시간: " + durationMilliseconds + " 밀리초");
        System.out.println("총 소요 시간: " + durationSeconds + " 초");
    }
}
