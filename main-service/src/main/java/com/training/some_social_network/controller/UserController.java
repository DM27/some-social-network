package com.training.some_social_network.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.some_social_network.dto.UserDto;
import com.training.some_social_network.security.jwt.JwtTokenProvider;
import com.training.some_social_network.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1", "/api/v2"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Simplified authentication process by passing a user ID and receiving a token for further authorization
     *
     * @param userDto user data
     * @return token
     */
    @PostMapping("/login")
    public LoginDto loginUser(@RequestBody UserDto userDto) {
        String userLogin = userDto.getId();
        String password = userDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin, password));

        return new LoginDto(jwtTokenProvider.getAccessToken(userLogin));
    }

    /**
     * Registering a new user
     *
     * @param userDto user data
     * @return user id
     */
    @PostMapping("/user/register")
    public RegisterDto registerUser(@RequestBody UserDto userDto) {
        return new RegisterDto(userService.register(userDto));
    }

    /**
     * Receiving a user data
     *
     * @param id user id
     * @return user data
     */
    @GetMapping("/user/get/{id}")
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
    @GetMapping("/user/search")
    public List<UserDto> searchUsers(@RequestParam("first_name") String firstName,
                                     @RequestParam("last_name") String secondName) {
        return userService.searchUsers(firstName, secondName);
    }

    /**
     * Adding a user's friend
     *
     * @param friendId     friend id
     */
    @PutMapping("/friend/set/{friendId}")
    public void addFriend(@PathVariable("friendId") String friendId) {
        userService.addFriend(friendId);
    }

    /**
     * Removing a user's friend
     *
     * @param friendId     friend id
     */
    @PutMapping("/friend/delete/{friendId}")
    public void deleteFriend(@PathVariable("friendId") String friendId) {
        userService.deleteFriend(friendId);
    }

    /**
     * An object containing a token upon successful authorization
     * Doesn't serve any purpose in this project
     *
     * @param token token
     */
    public record LoginDto(String token) { }

    /**
     * An object containing the user ID
     *
     * @param userId user id
     */
    public record RegisterDto(@JsonProperty("user_id") String userId) { }
}
