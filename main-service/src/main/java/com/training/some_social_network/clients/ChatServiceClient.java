package com.training.some_social_network.clients;

import com.training.some_social_network.dto.DialogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "dialog-service", url = "${chat-service.client.url}", configuration = ClientRequestInterceptor.class)
public interface ChatServiceClient {

    @PostMapping(value = "/internal/v1/dialog/{userId}/send", produces = MediaType.APPLICATION_JSON_VALUE)
    void addDialog(@PathVariable("userId") String userId, @RequestBody DialogDto dialogDto);

    @GetMapping(value = "/internal/v1/dialog/{userId}/list", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DialogDto> getUserDialogs(@PathVariable("userId") String userId);

}
