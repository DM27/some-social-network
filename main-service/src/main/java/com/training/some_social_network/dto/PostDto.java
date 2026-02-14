package com.training.some_social_network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.some_social_network.dao.PostDo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDto {

    private String id;
    private String text;
    @JsonProperty("author_user_id")
    private String authorUserId;

    public PostDto(PostDo postDo) {
        this.id = postDo.getId().toString();
        this.text = postDo.getText();
        this.authorUserId = postDo.getAuthorUserId().toString();
    }
}
