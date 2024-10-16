package com.sparta.springtrello.domain.board.dto;

import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardRequestDto{

    @NotBlank
    private String boardTitle;
    @NotBlank
    private String boardDescription;

}
