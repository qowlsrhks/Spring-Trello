package com.sparta.springtrello.domain.activity.dto;

import com.sparta.springtrello.domain.comment.dto.response.CommentResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CardActivityResponseDto {
    private List<CommentResponseDto> comments;
    private List<ActivityResponseDto> activities;

    public CardActivityResponseDto(List<CommentResponseDto> comment, List<ActivityResponseDto> activity) {
        this.comments = comment;
        this.activities = activity;
    }
}
