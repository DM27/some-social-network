package com.training.some_social_network.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class PostStateDto implements Serializable {

    private final UUID id;
    private final UUID authorId;
    private final PostState state;
}
