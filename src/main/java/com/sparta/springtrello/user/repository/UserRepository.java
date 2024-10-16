package com.sparta.springtrello.user.repository;

import com.sparta.springtrello.user.entity.Role;
import com.sparta.springtrello.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자를 찾는 메소드 (탈퇴 상태와 관계없이)
    Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByEmailAndIsDeletedFalse(String email);

    User findByRole(Role role);
}
