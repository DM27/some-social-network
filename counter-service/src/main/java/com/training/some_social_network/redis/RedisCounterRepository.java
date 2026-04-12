package com.training.some_social_network.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisCounterRepository {

    private static final String DIALOG_COUNTER_KEY_PREFIX = "dialog-user-counter:";

    private final HashOperations<String, String, Integer> counterOperations;
    private final RedisTemplate<String, Object> redisTemplate;

    public void increment(String userId, String fromUserId) {
        String userKey = DIALOG_COUNTER_KEY_PREFIX + userId;
        counterOperations.increment(userKey, fromUserId, 1);
    }

    public Long decrement(String userId, String fromUserId) {
        String userKey = DIALOG_COUNTER_KEY_PREFIX + userId;
        return counterOperations.increment(userKey, fromUserId, -1);
    }

    // todo: atomicity
    public Map<String, Integer> reset(String userId) {
        String userKey = DIALOG_COUNTER_KEY_PREFIX + userId;
        Map<String, Integer> entries = counterOperations.entries(userKey);
        redisTemplate.delete(userKey);
        return entries;
    }

    public void set(String userId, Map<String, Integer> counters) {
        String userKey = DIALOG_COUNTER_KEY_PREFIX + userId;
        counterOperations.putAll(userKey, counters);
    }

    public void reset(String userId, String fromUserId) {
        String userKey = DIALOG_COUNTER_KEY_PREFIX + userId;
        counterOperations.delete(userKey, fromUserId);
    }
}
