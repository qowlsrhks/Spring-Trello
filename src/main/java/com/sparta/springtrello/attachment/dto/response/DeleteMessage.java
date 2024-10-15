package com.sparta.springtrello.attachment.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class DeleteMessage {
    private String message;

    @Builder
    private DeleteMessage(String message) {
        this.message = message;
    }

    public static DeleteMessage of(String message) {
        return DeleteMessage.builder().message(message).build();
    }
}
