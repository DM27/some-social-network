package com.training.some_social_network.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "counter-service", url = "${counter-service.client.url}", configuration = ClientRequestInterceptor.class)
public interface CounterServiceClient {

    @GetMapping(value = "/internal/v1/dialog-counter/{userId}/get/{fromUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Integer getCounter(@PathVariable("userId") String userId, @PathVariable("fromUserId") String fromUserId);

    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/incr/{fromUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    void incrementCounter(@PathVariable("userId") String userId, @PathVariable("fromUserId") String fromUserId);

    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/decr/{fromUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    void decrementCounter(@PathVariable("userId") String userId, @PathVariable("fromUserId") String fromUserId);

    /**
     * @return all user counters
     */
    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Integer> resetCounters(@PathVariable("userId") String userId);

    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/set", produces = MediaType.APPLICATION_JSON_VALUE)
    void setCounters(@PathVariable("userId") String userId, @RequestBody Map<String, Integer> userCounters);
}
