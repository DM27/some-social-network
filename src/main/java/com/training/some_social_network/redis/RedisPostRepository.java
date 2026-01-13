package com.training.some_social_network.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RedisPostRepository {

    public static final String POST_KEY_PREFIX = "postFeed:";
    public static final Duration POST_CACHE_TTL = Duration.of(24, ChronoUnit.HOURS);

    private final RedisTemplate<String, String> redisTemplate;

    public List<UUID> getAllPostIds(UUID userId, int start, int end) {
        List<String> result = redisTemplate.opsForList().range(POST_KEY_PREFIX + userId, start, end);
        return result == null ? List.of() : result.stream().map(UUID::fromString).toList();
    }

    public void add(UUID userId, UUID postId) {
        String key = POST_KEY_PREFIX + userId;
        redisTemplate.opsForList().leftPush(key, postId.toString());
        redisTemplate.expire(key, POST_CACHE_TTL);
    }

    public void remove(UUID userId, UUID postId) {
        redisTemplate.opsForList().remove(POST_KEY_PREFIX + userId, 1, postId.toString());
    }

    public void addAll(UUID userId, List<UUID> userFeed) {
        if (!userFeed.isEmpty()) {
            List<String> posts = userFeed.stream().map(UUID::toString).toList();
            String key = POST_KEY_PREFIX + userId;
            redisTemplate.opsForList().leftPushAll(key, posts);
            redisTemplate.expire(key, POST_CACHE_TTL);
        }
    }

    public boolean isFeedExisted(UUID userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(POST_KEY_PREFIX + userId));
    }

    public void trim(UUID userId, int size) {
        redisTemplate.opsForList().trim(POST_KEY_PREFIX + userId, 0, size - 1);
    }
}
