package com.training.some_social_network.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "counter-service", url = "${counter-service.client.url}", configuration = ClientRequestInterceptor.class)
public interface CounterServiceClient {

    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/incr/{fromUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    void incrementCounter(@RequestParam("userId") String userId, @RequestParam("userId") String fromUserId);

    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/decr/{fromUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    void decrementCounter(@RequestParam("userId") String userId, @RequestParam("userId") String fromUserId);

    /**
     * @return all user counters
     */
    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Integer> resetCounters(String userId);

    @PostMapping(value = "/internal/v1/dialog-counter/{userId}/set", produces = MediaType.APPLICATION_JSON_VALUE)
    void setCounters(String userId, Map<String, Integer> userCounters);
}
