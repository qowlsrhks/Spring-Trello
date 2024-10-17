package com.sparta.springtrello.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String contents;

    @JsonCreator
    public CommentRequestDto(@JsonProperty("contents") String contents) {
        this.contents = contents;
    }
}
