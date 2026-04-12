package com.training.some_social_network.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.training.some_social_network.service.CounterService;

import java.util.Map;

@RestController
@RequestMapping("/internal/v1/dialog-counter")
@RequiredArgsConstructor
public class CounterController {

    private final CounterService counterService;

    /**
     * Increment user counter
     *
     * @param userId
     * @param fromUserId
     */
    @PostMapping("/{userId}/incr/{fromUserId}")
    public void increment(@PathVariable("userId") String userId, @PathVariable("fromUserId") String fromUserId) {
        counterService.increment(userId, fromUserId);
    }

    /**
     * Decrement user counter
     *
     * @param userId
     * @param fromUserId
     */
    @PostMapping("/{userId}/decr/{fromUserId}")
    public void decrement(@PathVariable("userId") String userId, @PathVariable("fromUserId") String fromUserId) {
        counterService.decrement(userId, fromUserId);
    }

    /**
     * Reset user counters
     *
     * @param userId
     * @return user counters
     */
    @PostMapping("/{userId}/reset")
    public Map<String, Integer> reset(@PathVariable("userId") String userId) {
        return counterService.reset(userId);
    }

    /**
     * Set user counters
     *
     * @param counters
     */
    @PostMapping("/{userId}/set")
    public void set(@PathVariable("userId") String userId, @RequestBody Map<String, Integer> counters) {
        counterService.set(userId, counters);
    }
}
