package com.sparta.springtrello.domain.workspaceMember.controller;


import com.sparta.springtrello.domain.user.entity.Role;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspaceMember.dto.InviteRoleRequsetDto;
import com.sparta.springtrello.domain.workspaceMember.entity.MemberRole;
import com.sparta.springtrello.domain.workspaceMember.service.WsmemberService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Wsmember")
public class WsmemberController {
    private final WsmemberService wsmemberService;

    @PostMapping("/invite")
    public String inviteRole(@RequestBody InviteRoleRequsetDto inviteRoleRequestDto) {
        try {
            MemberRole memberRole = MemberRole.valueOf(inviteRoleRequestDto.getRole().toUpperCase());
            wsmemberService.assignRole(inviteRoleRequestDto.getUserId(), memberRole);
            return "Role assigned successfully!";
        } catch (IllegalArgumentException e) {
            return "Invalid role specified.";
        }
    }
}
