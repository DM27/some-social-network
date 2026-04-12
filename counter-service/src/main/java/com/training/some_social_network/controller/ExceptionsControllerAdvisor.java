package com.training.some_social_network.controller;

import com.training.some_social_network.exceptions.NotFoundException;
import com.training.some_social_network.exceptions.NotValidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ExceptionsControllerAdvisor {

    @ExceptionHandler({NotValidDataException.class, BadCredentialsException.class})
    public ResponseEntity<String> handleBadRequest(Exception e) {
        LOG.warn("Not valid request", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(Exception e) {
        LOG.warn("Entity not found", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOG.warn("Internal server error", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
