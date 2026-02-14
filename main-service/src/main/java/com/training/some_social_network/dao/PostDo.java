package com.training.some_social_network.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PostDo {

    private UUID id;
    private String text;
    private UUID authorUserId;
}

