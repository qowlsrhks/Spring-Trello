package com.sparta.springtrello.domain.workspaceMember.service;

import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspaceMember.entity.MemberRole;
import com.sparta.springtrello.domain.workspaceMember.entity.Wsmember;
import com.sparta.springtrello.domain.workspaceMember.repository.WsmemberRepository;
import org.springframework.stereotype.Service;

@Service
public class WsmemberService {
    private final WsmemberRepository wsmemberRepository;

    public WsmemberService(WsmemberRepository wsmemberRepository) {
        this.wsmemberRepository = wsmemberRepository;
    }

    public void assignRole(String userId, MemberRole memberRole) {
        Wsmember wsmember = wsmemberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        wsmember.changeRole(memberRole);  // 역할 변경
        wsmemberRepository.save(wsmember);  // 변경 사항 저장
    }
}