package com.training.some_social_network.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class DialogDo {

    private UUID id;
    private UUID userFromId;
    private UUID userToId;
    private String message;
    private String shardKey;
}

