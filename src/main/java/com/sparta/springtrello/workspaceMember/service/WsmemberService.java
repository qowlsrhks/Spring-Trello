package com.sparta.springtrello.workspaceMember.service;

import com.sparta.springtrello.user.entity.User;
import com.sparta.springtrello.workspaceMember.entity.MemberRole;
import com.sparta.springtrello.workspaceMember.repository.WsmemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WsmemberService {
    private WsmemberRepository wsmemberRepository;

    plic void MemberRole(User user, MemberRole memberRole) {
        user.setRole(memberRole);
    }
}
