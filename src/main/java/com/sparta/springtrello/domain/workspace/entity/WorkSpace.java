package com.sparta.springtrello.domain.workspace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "workspace")
public class WorkSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workspaceId;

    @Column(name = "workspace_name", nullable = false)
    private String workspaceName;

    @Column(name = "workspace_description", nullable = false)
    private String workspaceDescription;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "workSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Board> boards = new ArrayList<>();

    public WorkSpace(WorkSpaceRequestDto workSpaceRequestDto){
        this.workspaceName = workSpaceRequestDto.getWorkspaceName();
        this.workspaceDescription = workSpaceRequestDto.getWorkspaceDescription();
        //this.createdAt = workSpaceRequestDto.getCreatedAt();
       // this.modifiedAt = workSpaceRequestDto.getModifiedAt();
    }

    public void update(WorkSpaceRequestDto workSpaceRequestDto){
        this.workspaceName = workSpaceRequestDto.getWorkspaceName();
        this.workspaceDescription = workSpaceRequestDto.getWorkspaceDescription();
        //this.modifiedAt = workSpaceRequestDto.getModifiedAt();
    }
}
