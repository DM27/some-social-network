package com.training.some_social_network.controller;

import com.training.some_social_network.dto.UserDto;
import com.training.some_social_network.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Simplified authentication process by passing a user ID and receiving a token for further authorization
     * Registration is nominal, without Security, and is not verified anywhere
     *
     * @param userDto user data
     * @return token
     */
    @PostMapping("/login")
    public LoginDto loginUser(@RequestBody UserDto userDto) {
        String userLogin = userDto.getUuid();
        String password = userDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin, password));

        return new LoginDto(jwtTokenProvider.getAccessToken(userLogin));
    }

    /**
     * An object containing a token upon successful authorization
     * Doesn't serve any purpose in this project
     *
     * @param token token
     */
    public record LoginDto(String token) { }
}
