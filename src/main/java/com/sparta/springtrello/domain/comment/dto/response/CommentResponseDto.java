package com.sparta.springtrello.domain.comment.dto.response;

import com.sparta.springtrello.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String username;
    private String contents;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.contents = comment.getContents();
    }
}
