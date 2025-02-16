package com.sparta.springtrello.domain.user.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;


import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이메일로 사용자 ID 설정

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private LocalDateTime signupTime;

    @Enumerated(EnumType.STRING)
    private Role role; // USER 또는 OWNER


    @Column
    private boolean isDeleted = false; // 탈퇴 여부

    public User(String password, String username, String email, Role role, boolean isDeleted) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.signupTime = LocalDateTime.now();
        this.role = role;
        this.isDeleted = isDeleted;
    }

    // 테스트를 하기 위함
    private User(Long user_id, String email, Role role) {
        this.id = user_id;
        this.email = email;
        this.role = role;
    }

}
