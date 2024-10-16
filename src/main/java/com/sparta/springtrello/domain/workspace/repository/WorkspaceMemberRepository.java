package com.sparta.springtrello.domain.workspace.repository;


import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    Optional<WorkspaceMember> findByWorkSpaceAndUser(WorkSpace workspace, User user);
}