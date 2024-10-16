package com.sparta.springtrello.domain.workspace.dto;

import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import lombok.Data;

@Data
public class WorkSpaceMemberRequestDto {

    private Long workSpaceId;
    private String workspaceName;
    private String workspaceDescription;

    public WorkSpaceMemberRequestDto(WorkspaceMember workSpaceMember) {
        this.workSpaceId = workSpaceMember.getWorkSpace().getWorkspaceId();
        this.workspaceName = workSpaceMember.getWorkSpace().getWorkspaceName();
        this.workspaceDescription = workSpaceMember.getWorkSpace().getWorkspaceDescription();
    }
}
