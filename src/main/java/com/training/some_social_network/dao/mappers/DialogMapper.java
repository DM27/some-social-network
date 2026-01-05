package com.training.some_social_network.dao.mappers;

import com.training.some_social_network.dao.DialogDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DialogMapper {

    void add(DialogDo dialogDo);

    List<DialogDo> findDialogs(@Param("shardKey") String shardKey);
}
