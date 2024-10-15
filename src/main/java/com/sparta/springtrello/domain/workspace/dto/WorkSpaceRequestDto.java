package com.sparta.springtrello.domain.workspace.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkSpaceRequestDto {

    private String workspaceName;
    private String workspaceDescription;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}
