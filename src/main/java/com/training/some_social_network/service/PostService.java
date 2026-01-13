package com.training.some_social_network.service;

import com.training.some_social_network.dao.PostDo;
import com.training.some_social_network.dao.mappers.PostMapper;
import com.training.some_social_network.dto.PostDto;
import com.training.some_social_network.dto.PostState;
import com.training.some_social_network.dto.PostStateDto;
import com.training.some_social_network.exceptions.NotValidDataException;
import com.training.some_social_network.rabbitmq.RabbitService;
import com.training.some_social_network.security.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final RabbitService rabbitService;
    private final FeedService feedService;

    @Transactional
    public PostDto create(String postText) {
        NotValidDataException.throwIf(postText == null, "Невалидные данные");

        Optional<UUID> userId = SecurityHelper.getCurrentUserId();
        NotValidDataException.throwIf(userId.isEmpty(), "Невалидные данные");

        PostDo postDo = new PostDo();
        postDo.setText(postText);
        postDo.setAuthorUserId(userId.get());

        postMapper.insertPost(postDo);

        rabbitService.sendPostState(new PostStateDto(postDo.getId(), userId.get(), PostState.CREATED));

        return new PostDto(postDo);
    }

    @Transactional
    public void update(PostDto postDto) {
        NotValidDataException.throwIf(postDto.getId() == null, "Невалидные данные");
        NotValidDataException.throwIf(postDto.getText() == null, "Невалидные данные");

        Optional<UUID> userId = SecurityHelper.getCurrentUserId();
        NotValidDataException.throwIf(userId.isEmpty(), "Невалидные данные");

        PostDo postDo = new PostDo();
        postDo.setId(UUID.fromString(postDto.getId()));
        postDo.setText(postDto.getText());
        postDo.setAuthorUserId(userId.get());

        postMapper.updatePost(postDo);

        rabbitService.sendPostState(new PostStateDto(postDo.getId(), userId.get(), PostState.UPDATED));
    }

    @Transactional
    public void delete(String id) {
        NotValidDataException.throwIf(id == null, "Невалидные данные");

        UUID postId = UUID.fromString(id);
        PostDo postDo = postMapper.getPost(postId);
        postMapper.deletePost(postId);

        rabbitService.sendPostState(new PostStateDto(postId, postDo.getAuthorUserId(), PostState.DELETED));
    }

    @Transactional(readOnly = true)
    public PostDto get(String id) {
        NotValidDataException.throwIf(id == null, "Невалидные данные");

        UUID postId = UUID.fromString(id);
        PostDo postDo = postMapper.getPost(postId);

        return postDo != null ? new PostDto(postDo) : null;
    }

    @Transactional(readOnly = true)
    public List<PostDto> getCurrentUserFeed(Integer offset, Integer limit) {
        NotValidDataException.throwIf(offset == null, "Невалидные данные");
        NotValidDataException.throwIf(limit == null, "Невалидные данные");

        Optional<UUID> userId = SecurityHelper.getCurrentUserId();
        NotValidDataException.throwIf(userId.isEmpty(), "Невалидные данные");

        List<UUID> postIds = feedService.getUserFeed(userId.get(), offset, limit);
        if (postIds.isEmpty()) {
            return List.of();
        }

        return postMapper.getPostByIds(postIds).stream()
                .map(PostDto::new)
                .toList();
    }
}
