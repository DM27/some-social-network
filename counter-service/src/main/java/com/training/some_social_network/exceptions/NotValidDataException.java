package com.training.some_social_network.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotValidDataException extends RuntimeException {

    public NotValidDataException(String message) {
        super(message);
    }

    public static void throwIf(boolean condition, String message) {
        if (condition) {
            throw new NotValidDataException(message);
        }
    }
}
