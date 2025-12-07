package com.training.some_social_network.dao.mappers;

import com.training.some_social_network.dao.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface UserMapper {

    UserDo getUserById(@Param("userId") UUID userId);

    void insertUser(UserDo userDo);

    List<UserDo> searchUsers(@Param("firstName") String firstName,
                             @Param("secondName") String secondName);
}
