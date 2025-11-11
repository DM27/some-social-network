package com.training.some_social_network.dao.mappers;

import com.training.some_social_network.dao.UserDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDo getUserByUuid(String uuid);

    void insertUser(UserDo userDo);
}
