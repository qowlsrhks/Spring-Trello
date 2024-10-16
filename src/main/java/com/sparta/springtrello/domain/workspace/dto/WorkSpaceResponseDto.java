package com.sparta.springtrello.domain.workspace.dto;

import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkSpaceResponseDto {

    private Long id;
    private String workspaceName;
    private String workspaceDescription;

    public WorkSpaceResponseDto(WorkSpace workSpace) {
        this.id = workSpace.getWorkspaceId();
        this.workspaceName = workSpace.getWorkspaceName();
        this.workspaceDescription = workSpace.getWorkspaceDescription();
    }
}
