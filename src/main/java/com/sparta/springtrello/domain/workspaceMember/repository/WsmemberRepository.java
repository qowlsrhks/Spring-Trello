package com.sparta.springtrello.domain.workspaceMember.repository;

import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspaceMember.entity.Wsmember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WsmemberRepository extends JpaRepository<Wsmember, Long> {
    Optional<Wsmember> findByUserId(String userId);
}