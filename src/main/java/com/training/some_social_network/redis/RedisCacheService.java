package com.training.some_social_network.redis;

import com.training.some_social_network.dao.mappers.PostMapper;
import com.training.some_social_network.dao.mappers.UserMapper;
import com.training.some_social_network.exceptions.NotValidDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * todo: Use a lua script for atomic operations
 */
@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private static final int FEED_MAX_SIZE = 1000;

    private final RedisPostRepository redisPostRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    public List<UUID> getUserFeed(UUID userId, int offset, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        initFeedIfNeeded(userId);
        return redisPostRepository.getAllPostIds(userId, offset, offset + limit - 1);
    }

    public void addToFeed(UUID postId, UUID authorId) {
        NotValidDataException.throwIf(postId == null, "Невалидные данные");
        NotValidDataException.throwIf(authorId == null, "Невалидные данные");

        List<UUID> friendIds = userMapper.findFriends(authorId);
        for (UUID friend : friendIds) {
            if (initFeedIfNeeded(friend)) {
                redisPostRepository.add(friend, postId);
                redisPostRepository.trim(friend, FEED_MAX_SIZE);
            }
        }
    }

    public void updateFeed(UUID postId, UUID authorId) {
        NotValidDataException.throwIf(postId == null, "Невалидные данные");
        NotValidDataException.throwIf(authorId == null, "Невалидные данные");

        List<UUID> friendIds = userMapper.findFriends(authorId);
        for (UUID friend : friendIds) {
            if (initFeedIfNeeded(friend)) {
                redisPostRepository.remove(friend, postId);
                redisPostRepository.add(friend, postId);
            }
        }
    }

    public void removeFromFeed(UUID postId, UUID authorId) {
        NotValidDataException.throwIf(postId == null, "Невалидные данные");
        NotValidDataException.throwIf(authorId == null, "Невалидные данные");

        List<UUID> friendIds = userMapper.findFriends(authorId);
        for (UUID friend : friendIds) {
            if (initFeedIfNeeded(friend)) {
                redisPostRepository.remove(friend, postId);
            }
        }
    }

    /**
     * @return true - if the cache existed, false - if initialized now
     */
    private boolean initFeedIfNeeded(UUID userId) {
        if (redisPostRepository.isFeedExisted(userId)) {
            return true;
        }
        // bad practice, but it's suitable for a mock project
        synchronized (userId.toString().intern()) {
            if (!redisPostRepository.isFeedExisted(userId)) {
                List<UUID> userFeed = postMapper.getUserFeed(userId, FEED_MAX_SIZE);
                redisPostRepository.addAll(userId, userFeed);
            }
            return false;
        }
    }
}
