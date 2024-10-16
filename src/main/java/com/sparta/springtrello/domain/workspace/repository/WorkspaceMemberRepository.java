package com.sparta.springtrello.domain.workspace.repository;


import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    List<WorkspaceMember>findByUserEmail(String email);
    List<WorkspaceMember>findByUserEmailAndWorkSpace_WorkspaceId(String email ,Long workSpaceId);
    List<WorkspaceMember> findByRole(MemberRole role);
}