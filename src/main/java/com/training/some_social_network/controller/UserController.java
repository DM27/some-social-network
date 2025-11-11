package com.training.some_social_network.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.some_social_network.dto.UserDto;
import com.training.some_social_network.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registering a new user
     *
     * @param userDto user data
     * @return user id
     */
    @PostMapping("/register")
    public RegisterDto registerUser(@RequestBody UserDto userDto) {
        return new RegisterDto(userService.register(userDto));
    }

    /**
     * Receiving a user data
     *
     * @param id user id
     * @return user data
     */
    @GetMapping("/get/{id}")
    public UserDto getUserById(@PathVariable String id) {
        return userService.obtainUserByUuid(id);
    }

    /**
     * An object containing the user ID
     *
     * @param userId user id
     */
    public record RegisterDto(@JsonProperty("user_id") String userId) { }
}
