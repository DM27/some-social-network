package com.training.some_social_network.service;

import com.training.some_social_network.clients.ChatServiceClient;
import com.training.some_social_network.clients.CounterServiceClient;
import com.training.some_social_network.dto.DialogDto;
import com.training.some_social_network.exceptions.NotValidDataException;
import com.training.some_social_network.security.SecurityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogService {

    private final ChatServiceClient chatServiceClient;
    private final CounterServiceClient counterServiceClient;

    @Transactional
    public void addDialog(String userId, DialogDto dialogDto) {
        UUID currentUserId = SecurityHelper.getCurrentUserId().orElse(null);
        NotValidDataException.throwIf(currentUserId == null, "Невалидные данные");

        String userFromId = currentUserId.toString();
        dialogDto.setUserFromId(userFromId);
        dialogDto.setUserToId(userId);

        counterServiceClient.incrementCounter(userId, userFromId);

        try {
            chatServiceClient.addDialog(userId, dialogDto);
        } catch (Exception ex) {
            counterServiceClient.decrementCounter(userId, userFromId);
            LOG.error("Error adding dialog for user: {}", userId, ex);
        }
    }

    public List<DialogDto> getUserDialogs(String userId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");

        Optional<UUID> currentUserId = SecurityHelper.getCurrentUserId();
        NotValidDataException.throwIf(currentUserId.isEmpty(), "Невалидные данные");

        Map<String, Integer> userCounters = counterServiceClient.resetCounters(userId);

        try {
            return chatServiceClient.getUserDialogs(userId);
        } catch (Exception ex) {
            counterServiceClient.setCounters(userId, userCounters);
            LOG.error("Error get user {} dialogs ", userId, ex);
            throw ex;
        }
    }
}
