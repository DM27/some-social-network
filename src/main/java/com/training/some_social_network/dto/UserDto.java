package com.training.some_social_network.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.some_social_network.dao.UserDo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = "password", allowSetters = true)
public class UserDto {

    private String id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("second_name")
    private String secondName;
    private LocalDate birthdate;
    private Integer gender;
    private String biography;
    private String city;
    private String password;

    public UserDto(UserDo user) {
        id = user.getId().toString();
        firstName = user.getFirstName();
        secondName = user.getSecondName();
        birthdate = user.getBirthdate();
        gender = user.getGender();
        biography = user.getBiography();
        city = user.getCity();
        password = "";
    }
}
