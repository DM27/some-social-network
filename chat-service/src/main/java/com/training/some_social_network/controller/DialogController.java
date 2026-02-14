package com.training.some_social_network.controller;

import com.training.some_social_network.dto.DialogDto;
import com.training.some_social_network.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/v1/dialog")
@RequiredArgsConstructor
public class DialogController {

    private final DialogService dialogService;

    /**
     * Saving user message
     *
     * @param userId    recipient user id
     * @param dialogDto dialog data
     */
    @PostMapping("/{userId}/send")
    public void sendDialog(@PathVariable("userId") String userId, @RequestBody DialogDto dialogDto) {
        dialogService.addDialog(userId, dialogDto);
    }

    /**
     * Getting a list of dialogs with the user
     *
     * @param userId  recipient user id
     * @return  list of dialogs
     */
    @GetMapping("/{userId}/list")
    public List<DialogDto> getDialogs(@PathVariable("userId") String userId) {
        return dialogService.getUserDialogs(userId);
    }
}
