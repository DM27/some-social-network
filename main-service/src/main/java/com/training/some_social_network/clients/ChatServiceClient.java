package com.training.some_social_network.clients;

import com.training.some_social_network.dto.DialogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "dialog-service", url = "${chat-service.client.url}", configuration = ClientRequestInterceptor.class)
public interface ChatServiceClient {

    @PostMapping(value = "/internal/v1/dialog/{userId}/send", produces = MediaType.APPLICATION_JSON_VALUE)
    void addDialog(@RequestParam("userId") String userId, @RequestBody DialogDto dialogDto);

    @GetMapping(value = "/internal/v1/dialog/{userId}/list", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DialogDto> getUserDialogs(@RequestParam("userId") String userId);

}
