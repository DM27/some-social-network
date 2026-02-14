package com.training.some_social_network.dao.mappers;

import com.training.some_social_network.dao.PostDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface PostMapper {

    void insertPost(PostDo postDo);

    void updatePost(PostDo postDo);

    void deletePost(@Param("postId") UUID postId);

    PostDo getPost(@Param("postId") UUID postId);

    List<PostDo> getPostByIds(List<UUID> postIds);

    List<UUID> getUserFeed(@Param("userId") UUID userId, @Param("limit") int limit);
}
