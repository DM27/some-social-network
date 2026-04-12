package com.training.some_social_network.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static void throwIf(boolean condition, String message) {
        if (condition) {
            throw new NotFoundException(message);
        }
    }
}
