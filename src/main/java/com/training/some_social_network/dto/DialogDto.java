package com.training.some_social_network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.some_social_network.dao.DialogDo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DialogDto {

    private String id;
    @JsonProperty("from")
    private String userFromId;
    @JsonProperty("to")
    private String userToId;
    @JsonProperty("text")
    private String message;

    public DialogDto(DialogDo dialogDo) {
        this.id = dialogDo.getId().toString();
        this.userFromId = dialogDo.getUserFromId().toString();
        this.userToId = dialogDo.getUserFromId().toString();
        this.message = dialogDo.getMessage();
    }
}
