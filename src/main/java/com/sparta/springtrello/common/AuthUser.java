package com.sparta.springtrello.common;

import com.sparta.springtrello.user.entity.Role;
import lombok.Getter;

@Getter
public class AuthUser {
    private final String email;
    private final Role role;
    private final Long userId;

    public AuthUser(String email, Role role, Long userId) {
        this.email = email;
        this.role = role;
        this.userId = userId;
    }
}
