package com.training.some_social_network.service;

import com.training.some_social_network.dao.PostDo;
import com.training.some_social_network.dao.mappers.PostMapper;
import com.training.some_social_network.dao.mappers.UserMapper;
import com.training.some_social_network.dto.PostDto;
import com.training.some_social_network.exceptions.NotValidDataException;
import com.training.some_social_network.redis.RedisFeedService;
import com.training.some_social_network.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * todo: Use a lua script for atomic operations
 */
@Service
@RequiredArgsConstructor
public class FeedService {

    public static final int FEED_MAX_SIZE = 1000;
    public static final int CELEBRITY_SIZE = 100;

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final RedisFeedService redisFeedService;
    private final WebSocketService webSocketService;

    public List<UUID> getUserFeed(UUID userId, int offset, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        NotValidDataException.throwIf(userId == null, "Невалидные данные");

        initFeedIfNeeded(userId);
        return redisFeedService.getUserFeed(userId, offset, limit);
    }

    public void addToFeed(UUID postId, UUID authorId) {
        NotValidDataException.throwIf(postId == null, "Невалидные данные");
        NotValidDataException.throwIf(authorId == null, "Невалидные данные");

        List<UUID> friendIds = userMapper.findFriends(authorId);

        boolean isCelebrity = friendIds.size() > CELEBRITY_SIZE;
        for (UUID friend : friendIds) {
            if (isCelebrity && !webSocketService.isUserConnected(friend)) {
                // todo Actualize the feed on connect
                continue;
            }
            if (initFeedIfNeeded(friend)) {
                redisFeedService.addToFeed(postId, friend);
            }
            notifyUser(postId, friend);
        }
    }

    public void removeFromFeed(UUID postId, UUID authorId) {
        NotValidDataException.throwIf(postId == null, "Невалидные данные");
        NotValidDataException.throwIf(authorId == null, "Невалидные данные");

        List<UUID> friendIds = userMapper.findFriends(authorId);
        for (UUID friend : friendIds) {
            if (initFeedIfNeeded(friend)) {
                redisFeedService.removeFromFeed(postId, friend);
            }
        }
    }

    private void notifyUser(UUID postId, UUID friend) {
        PostDo postDo = postMapper.getPost(postId);
        if (postDo != null) {
            PostDto postDto = new PostDto(postDo);
            webSocketService.sendNotification(friend, postDto);
        }
    }

    /**
     * @return true - if the cache existed, false - if initialized now
     */
    private boolean initFeedIfNeeded(UUID userId) {
        if (redisFeedService.isFeedExisted(userId)) {
            return true;
        }
        // bad practice, but it's suitable for a mock project
        synchronized (userId.toString().intern()) {
            if (!redisFeedService.isFeedExisted(userId)) {
                List<UUID> userFeed = postMapper.getUserFeed(userId, FEED_MAX_SIZE);
                redisFeedService.addToFeed(userId, userFeed);
            }
            return false;
        }
    }
}
