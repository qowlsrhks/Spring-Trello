package com.sparta.springtrello.domain.workspace.dto;

import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkSpaceResponseDto {
    private String workspaceName;
    private String workspaceDescription;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public WorkSpaceResponseDto(WorkSpace workSpace) {
        this.workspaceName = workSpace.getWorkspaceName();
        this.workspaceDescription = workSpace.getWorkspaceDescription();
        this.createdAt = workSpace.getCreatedAt();
        this.modifiedAt = workSpace.getModifiedAt();
    }
}
