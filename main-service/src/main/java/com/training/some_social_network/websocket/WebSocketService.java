package com.training.some_social_network.websocket;

import com.training.some_social_network.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;

    public void sendNotification(UUID userId, PostDto postDto) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/post/feed/posted", postDto);
    }

    public boolean isUserConnected(UUID userId) {
        return userRegistry.getUser(userId.toString()) != null;
    }
}
