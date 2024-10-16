package com.sparta.springtrello.domain.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkSpaceRequestDto {

    @NotBlank
    private String workspaceName;
    @NotBlank
    private String workspaceDescription;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

}
