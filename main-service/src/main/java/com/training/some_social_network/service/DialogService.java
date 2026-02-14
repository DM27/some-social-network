package com.training.some_social_network.service;

import com.training.some_social_network.clients.ChatServiceClient;
import com.training.some_social_network.dto.DialogDto;
import com.training.some_social_network.exceptions.NotValidDataException;
import com.training.some_social_network.security.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DialogService {

    private final ChatServiceClient chatServiceClient;

    @Transactional
    public void addDialog(String userId, DialogDto dialogDto) {
        UUID currentUserId = SecurityHelper.getCurrentUserId().orElse(null);
        NotValidDataException.throwIf(currentUserId == null, "Невалидные данные");

        dialogDto.setUserFromId(currentUserId.toString());
        dialogDto.setUserToId(userId);

        chatServiceClient.addDialog(userId, dialogDto);
    }

    @Transactional(readOnly = true)
    public List<DialogDto> getUserDialogs(String userId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");

        Optional<UUID> currentUserId = SecurityHelper.getCurrentUserId();
        NotValidDataException.throwIf(currentUserId.isEmpty(), "Невалидные данные");

        return chatServiceClient.getUserDialogs(userId);
    }
}
