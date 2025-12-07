package com.training.some_social_network.dao;

import com.training.some_social_network.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDo {

    private UUID id;
    private String firstName;
    private String secondName;
    private LocalDate birthdate;
    private Integer gender;
    private String biography;
    private String city;
    private String password;

    public UserDo(UserDto userDto) {
        firstName = userDto.getFirstName();
        secondName = userDto.getSecondName();
        birthdate = userDto.getBirthdate();
        gender = userDto.getGender();
        biography = userDto.getBiography();
        city = userDto.getCity();
        password = "";
    }
}

