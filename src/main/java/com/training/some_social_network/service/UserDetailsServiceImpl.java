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

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userUuid) {
        NotValidDataException.throwIf(userUuid == null, "Невалидные данные");

        UserDo userDo = userMapper.getUserByUuid(userUuid);
        NotFoundException.throwIf(userDo == null, "Анкета не найдена");

        String authenticatedUsername = userDo.getUuid();
        String authenticatedPassword = userDo.getPassword();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");

        return new User(authenticatedUsername, authenticatedPassword, List.of(grantedAuthority));
    }
}