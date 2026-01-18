package com.training.some_social_network.redis;

import com.training.some_social_network.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * todo: Use a lua script for atomic operations
 */
@Service
@RequiredArgsConstructor
public class RedisFeedService {

    private final RedisFeedRepository redisFeedRepository;

    public List<UUID> getUserFeed(UUID userId, int offset, int limit) {
        return redisFeedRepository.getAllPostIds(userId, offset, offset + limit - 1);
    }

    public void addToFeed(UUID postId, UUID userId) {
        redisFeedRepository.add(userId, postId);
        redisFeedRepository.trim(userId, FeedService.FEED_MAX_SIZE);
    }

    public void removeFromFeed(UUID postId, UUID userId) {
        redisFeedRepository.remove(userId, postId);
    }

    public boolean isFeedExisted(UUID userId) {
        return redisFeedRepository.isFeedExisted(userId);
    }

    public void addToFeed(UUID userId, List<UUID> userFeed) {
        redisFeedRepository.addAll(userId, userFeed);
    }
}
