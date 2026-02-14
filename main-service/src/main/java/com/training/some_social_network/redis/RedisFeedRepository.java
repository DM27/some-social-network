package com.training.some_social_network.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RedisFeedRepository {

    private static final String POST_KEY_PREFIX = "postFeed:";
    private static final Duration POST_CACHE_TTL = Duration.of(24, ChronoUnit.HOURS);

    @Qualifier("redisStringTemplate")
    private final RedisTemplate<String, String> redisStringTemplate;

    public List<UUID> getAllPostIds(UUID userId, int start, int end) {
        List<String> result = redisStringTemplate.opsForList().range(POST_KEY_PREFIX + userId, start, end);
        return result == null ? List.of() : result.stream().map(UUID::fromString).toList();
    }

    public void add(UUID userId, UUID postId) {
        String key = POST_KEY_PREFIX + userId;
        redisStringTemplate.opsForList().leftPush(key, postId.toString());
        redisStringTemplate.expire(key, POST_CACHE_TTL);
    }

    public void remove(UUID userId, UUID postId) {
        redisStringTemplate.opsForList().remove(POST_KEY_PREFIX + userId, 1, postId.toString());
    }

    public void addAll(UUID userId, List<UUID> userFeed) {
        if (!userFeed.isEmpty()) {
            List<String> posts = userFeed.stream().map(UUID::toString).toList();
            String key = POST_KEY_PREFIX + userId;
            redisStringTemplate.opsForList().leftPushAll(key, posts);
            redisStringTemplate.expire(key, POST_CACHE_TTL);
        }
    }

    public boolean isFeedExisted(UUID userId) {
        return Boolean.TRUE.equals(redisStringTemplate.hasKey(POST_KEY_PREFIX + userId));
    }

    public void trim(UUID userId, int size) {
        redisStringTemplate.opsForList().trim(POST_KEY_PREFIX + userId, 0, size - 1);
    }
}
