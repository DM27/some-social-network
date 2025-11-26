package com.training.some_social_network.dao.mappers;

import com.training.some_social_network.dao.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDo getUserByUuid(String uuid);

    void insertUser(UserDo userDo);

    List<UserDo> searchUsers(@Param("firstName") String firstName,
                             @Param("secondName") String secondName);
}
