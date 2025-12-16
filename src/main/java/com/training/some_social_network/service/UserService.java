package com.training.some_social_network.service;

import com.training.some_social_network.dao.UserDo;
import com.training.some_social_network.dao.mappers.UserMapper;
import com.training.some_social_network.dto.UserDto;
import com.training.some_social_network.exceptions.NotFoundException;
import com.training.some_social_network.exceptions.NotValidDataException;
import com.training.some_social_network.security.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String register(UserDto userDto) {
        NotValidDataException.throwIf(userDto == null, "Невалидные данные");
        NotValidDataException.throwIf(userDto.getPassword() == null, "Невалидные данные");
        NotValidDataException.throwIf(userDto.getPassword().isEmpty(), "Невалидные данные");

        UserDo newUser = new UserDo(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userMapper.insertUser(newUser);

        return newUser.getId().toString();
    }

    @Transactional(readOnly = true)
    public UserDto obtainUserById(String userId) {
        NotValidDataException.throwIf(userId == null, "Невалидные данные");

        UUID uuid = UUID.fromString(userId);

        UserDo user = userMapper.getUserById(uuid);
        NotFoundException.throwIf(user == null, "Анкета не найдена");

        return new UserDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> searchUsers(String firstName, String secondName) {
        NotValidDataException.throwIf(firstName == null, "Невалидные данные");
        NotValidDataException.throwIf(secondName == null, "Невалидные данные");

        List<UserDo> users = userMapper.searchUsers(firstName, secondName);

        return users.stream()
                .map(UserDto::new)
                .toList();
    }

    @Transactional
    public void addFriend(String friendId) {
        NotValidDataException.throwIf(friendId == null, "Невалидные данные");

        Optional<UUID> userId = SecurityHelper.getCurrentUserId();
        NotValidDataException.throwIf(userId.isEmpty(), "Невалидные данные");

        userMapper.addFriend(userId.get(), UUID.fromString(friendId));
    }

    @Transactional
    public void deleteFriend(String friendId) {
        NotValidDataException.throwIf(friendId == null, "Невалидные данные");
        userMapper.deleteFriend(UUID.fromString(friendId));
    }
}
