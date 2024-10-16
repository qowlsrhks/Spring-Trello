package com.sparta.springtrello.domain.workspace.entity;

import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "workspace_member")
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private com.sparta.springtrello.domain.workspace.entity.MemberRole role; // MemberRole Enum 사용

    public WorkspaceMember(WorkSpace workSpace, User user, MemberRole role) {
        this.workSpace = workSpace;
        this.user = user;
        this.role = role;
    }
}
