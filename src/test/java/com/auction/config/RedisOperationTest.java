package com.auction.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ActiveProfiles("dev")
class RedisOperationTest {
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void beforeEach() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
                "localhost", 6379
        );
        lettuceConnectionFactory.afterPropertiesSet();

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.afterPropertiesSet();
    }

    @Test
    @DisplayName("redisTemplate - auction room test")
    void redisTemplateTest() {
        AuctionRoom auctionRoom = new AuctionRoom();
        auctionRoom.setUuid(UUID.randomUUID());
        auctionRoom.setEndPrice(1000.0);
        auctionRoom.setStartPrice(1000.0);
        auctionRoom.setCurrentUserId(0L);
        auctionRoom.setStartTimestamp(System.currentTimeMillis());
        auctionRoom.setEndTimestamp(System.currentTimeMillis() + 3600 * 1000);

        redisTemplate.opsForHash().putAll(auctionRoom.toKeyString(), auctionRoom.toValueMap());
    }


    // Test Class
    class AuctionRoom {
        private UUID uuid;
        private Double startPrice = 0.;
        private Double endPrice = 0.;
        private Long currentUserId = 0L;
        private Long startTimestamp = 0L;
        private Long endTimestamp = 0L;

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public AuctionRoom() {
        }

        public AuctionRoom(UUID uuid, Double startPrice, Double endPrice, Long currentUserId, Long startTimestamp, Long endTimestamp) {
            this.uuid = uuid;
            this.startPrice = startPrice;
            this.endPrice = endPrice;
            this.currentUserId = currentUserId;
            this.startTimestamp = startTimestamp;
            this.endTimestamp = endTimestamp;
        }

        public String toKeyString() {
            return "auction:room:" + uuid.toString();
        }

        public Map<String, String> toValueMap() {
            Map<String, String> valueMap = new HashMap<>();
            valueMap.put("startPrice", startPrice.toString());
            valueMap.put("endPrice", endPrice.toString());
            valueMap.put("currentUserId", currentUserId.toString());
            valueMap.put("startTimestamp", startTimestamp.toString());
            valueMap.put("endTimestamp", endTimestamp.toString());
            return valueMap;
        }


        public Double getStartPrice() {
            return startPrice;
        }

        public void setStartPrice(Double startPrice) {
            this.startPrice = startPrice;
        }

        public Double getEndPrice() {
            return endPrice;
        }

        public void setEndPrice(Double endPrice) {
            this.endPrice = endPrice;
        }

        public Long getCurrentUserId() {
            return currentUserId;
        }

        public void setCurrentUserId(Long currentUserId) {
            this.currentUserId = currentUserId;
        }

        public Long getStartTimestamp() {
            return startTimestamp;
        }

        public void setStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
        }

        public Long getEndTimestamp() {
            return endTimestamp;
        }

        public void setEndTimestamp(Long endTimestamp) {
            this.endTimestamp = endTimestamp;
        }
    }
}