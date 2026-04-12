package com.training.some_social_network.service;

import com.training.some_social_network.exceptions.NotValidDataException;
import com.training.some_social_network.redis.RedisCounterRepository;
import com.training.some_social_network.security.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final RedisCounterRepository counterRepository;

    public void increment(String userId, String fromUserId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");
        NotValidDataException.throwIf(fromUserId == null, "Невалидные данные");

        UUID currentUserId = SecurityHelper.getCurrentUserId().orElse(null);
        NotValidDataException.throwIf(currentUserId == null, "Невалидные данные");

        counterRepository.increment(userId, fromUserId);
    }

    public void decrement(String userId, String fromUserId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");
        NotValidDataException.throwIf(fromUserId == null, "Невалидные данные");

        UUID currentUserId = SecurityHelper.getCurrentUserId().orElse(null);
        NotValidDataException.throwIf(currentUserId == null, "Невалидные данные");

        Long value = counterRepository.decrement(userId, fromUserId);
        if (value < 0) {
            counterRepository.reset(userId, fromUserId);
        }
    }

    public Map<String, Integer> reset(String userId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");

        UUID currentUserId = SecurityHelper.getCurrentUserId().orElse(null);
        NotValidDataException.throwIf(currentUserId == null, "Невалидные данные");

        return counterRepository.reset(userId);
    }

    public void set(String userId, Map<String, Integer> counters) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");
        NotValidDataException.throwIf(counters == null, "Невалидные данные");

        UUID currentUserId = SecurityHelper.getCurrentUserId().orElse(null);
        NotValidDataException.throwIf(currentUserId == null, "Невалидные данные");

        if (!counters.isEmpty()) {
            counterRepository.set(userId, counters);
        }
    }
}
