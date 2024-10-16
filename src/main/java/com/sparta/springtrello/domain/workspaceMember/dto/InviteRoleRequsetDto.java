package com.sparta.springtrello.domain.workspaceMember.dto;

import com.sparta.springtrello.domain.user.entity.User;
import lombok.Getter;

@Getter
public class InviteRoleRequsetDto {
    private String userId;
    private String role;
}