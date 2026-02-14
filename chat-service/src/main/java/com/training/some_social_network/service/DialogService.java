package com.training.some_social_network.service;

import com.training.some_social_network.dao.DialogDo;
import com.training.some_social_network.dao.mappers.DialogMapper;
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

    private final DialogMapper dialogMapper;

    @Transactional
    public void addDialog(String userId, DialogDto dialogDto) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");
        NotValidDataException.throwIf(dialogDto == null, "Невалидные данные");
        NotValidDataException.throwIf(dialogDto.getMessage() == null, "Невалидные данные");

        UUID currentUserId = SecurityHelper.getCurrentUserId().orElse(null);
        NotValidDataException.throwIf(currentUserId == null, "Невалидные данные");

        UUID otherUserId = UUID.fromString(userId);
        String shardKey = getDialogShardKey(currentUserId, otherUserId);

        DialogDo dialogDo = new DialogDo();
        dialogDo.setUserFromId(currentUserId);
        dialogDo.setUserToId(otherUserId);
        dialogDo.setShardKey(shardKey);
        dialogDo.setMessage(dialogDto.getMessage());

        dialogMapper.add(dialogDo);
    }

    @Transactional(readOnly = true)
    public List<DialogDto> getUserDialogs(String userId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");

        Optional<UUID> currentUserId = SecurityHelper.getCurrentUserId();
        NotValidDataException.throwIf(currentUserId.isEmpty(), "Невалидные данные");

        UUID otherUserId = UUID.fromString(userId);
        String shardKey = getDialogShardKey(currentUserId.get(), otherUserId);
        return dialogMapper.findDialogs(shardKey).stream()
                .map(DialogDto::new)
                .toList();
    }

    private String getDialogShardKey(UUID currentUserId, UUID otherUserId) {
        return currentUserId.compareTo(otherUserId) > 0 ? currentUserId + "_" + otherUserId : otherUserId + "_" + currentUserId;
    }
}
