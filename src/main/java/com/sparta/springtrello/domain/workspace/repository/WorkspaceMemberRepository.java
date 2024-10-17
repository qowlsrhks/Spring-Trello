package com.sparta.springtrello.domain.workspace.repository;


import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    List<WorkspaceMember>findByUserEmail(String email);
    List<WorkspaceMember>findByUserEmailAndWorkSpace_WorkspaceId(String email ,Long workSpaceId);
    List<WorkspaceMember> findByRole(MemberRole role);

    Optional<WorkspaceMember> findByWorkSpaceAndUser(WorkSpace workSpace, User user);

}