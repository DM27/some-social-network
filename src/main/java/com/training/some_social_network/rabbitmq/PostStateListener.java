package com.training.some_social_network.rabbitmq;

import com.training.some_social_network.dto.PostStateDto;
import com.training.some_social_network.redis.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostStateListener {

    private final RedisCacheService redisService;

    /**
     * todo: Add error handling
     */
    @RabbitListener(queues = RabbitConfiguration.postQueueName)
    public void processPostQueue(PostStateDto postState) {
        if (postState == null || postState.getState() == null) {
            return;
        }

        switch (postState.getState()) {
            case CREATED -> redisService.addToFeed(postState.getId(), postState.getAuthorId());
            case UPDATED -> redisService.updateFeed(postState.getId(), postState.getAuthorId());
            case DELETED -> redisService.removeFromFeed(postState.getId(), postState.getAuthorId());
        }
    }
}
