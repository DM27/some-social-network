package com.training.some_social_network.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.some_social_network.dto.UserDto;
import com.training.some_social_network.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return userService.obtainUserById(id);
    }

    /**
     * Search for users by parts of their first and last names
     *
     * @param firstName     part of the first name to search for
     * @param secondName    part of the second name to search for
     * @return a list of users
     */
    @GetMapping("/search")
    public List<UserDto> searchUsers(@RequestParam("first_name") String firstName,
                                     @RequestParam("last_name") String secondName) {
        return userService.searchUsers(firstName, secondName);
    }

    /**
     * An object containing the user ID
     *
     * @param userId user id
     */
    public record RegisterDto(@JsonProperty("user_id") String userId) { }
}
