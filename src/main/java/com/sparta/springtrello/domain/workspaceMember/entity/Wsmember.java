package com.sparta.springtrello.domain.workspaceMember.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Wsmember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // 역할을 생성자로 설정
    public Wsmember(String userId, MemberRole role) {
        this.userId = userId;
        this.role = role;
    }

    // 역할을 변경할 수 있는 메소드 제공
    public void changeRole(MemberRole role) {
        this.role = role;
    }
}