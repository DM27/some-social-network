package com.training.some_social_network.service;

import com.training.some_social_network.dao.UserDo;
import com.training.some_social_network.dao.mappers.UserMapper;
import com.training.some_social_network.exceptions.NotFoundException;
import com.training.some_social_network.exceptions.NotValidDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");

        UUID uuid = UUID.fromString(userId);
        UserDo userDo = userMapper.getUserById(uuid);
        NotFoundException.throwIf(userDo == null, "Анкета не найдена");

        String authenticatedUsername = userDo.getId().toString();
        String authenticatedPassword = userDo.getPassword();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");

        return new User(authenticatedUsername, authenticatedPassword, List.of(grantedAuthority));
    }
}