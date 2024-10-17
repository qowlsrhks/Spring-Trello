package com.sparta.springtrello.domain.workspace.service;


import com.sparta.springtrello.domain.common.AuthUser;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceMemberRequestDto;
import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import com.sparta.springtrello.domain.user.entity.Role;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceResponseDto;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.repository.WorkSpaceRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    //    생성
    @Transactional
    public WorkSpaceResponseDto createWorkSpace(AuthUser authUser, WorkSpaceRequestDto workSpaceRequestDto) {

        User user = userRepository.findById(authUser.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 없습니다."));

        // ADMIN 권한 확인
        if (user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("권한이 없습니다. 워크스페이스를 생성할 수 없습니다.");
        }

        WorkSpace workSpace = new WorkSpace(workSpaceRequestDto);
        workSpace.setUser(user); // 워크스페이스 생성자 설정
        workSpaceRepository.save(workSpace);

        // 생성한 유저를 워크스페이스의 관리자(member)로 추가
        WorkspaceMember workspaceMember = new WorkspaceMember(workSpace, user, MemberRole.ADMIN);
        workspaceMemberRepository.save(workspaceMember);

        return new WorkSpaceResponseDto(workSpace);
    }

    //    수정
    public WorkSpaceResponseDto updateWorkSpace(AuthUser authUser, Long workSpaceId, WorkSpaceRequestDto workSpaceRequestDto) {
        WorkSpace workSpace = validateWorkSpace(authUser, workSpaceId);

        workSpace.update(workSpaceRequestDto);
        workSpaceRepository.save(workSpace);
        return new WorkSpaceResponseDto(workSpace);
    }

    //    단건조회
    public WorkSpaceResponseDto getWorkSpace(AuthUser authUser, Long workSpaceId) {
        WorkSpace workSpace = validateWorkSpace(authUser, workSpaceId);
        return new WorkSpaceResponseDto(workSpace);

    }

    //    다건조회
    public List<WorkSpaceMemberRequestDto> getWorkSpaces(AuthUser authUser) {
       User loggedInUser = userRepository.findById(authUser.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 없습니다."));
        List<WorkspaceMember> workSpaces = workspaceMemberRepository.findByUserEmail(loggedInUser.getEmail());
        if(workSpaces.isEmpty()) {
            throw new IllegalArgumentException("역할이 있는 워크스페이스가 없습니다");
        }

        return workSpaces.stream().map(WorkSpaceMemberRequestDto::new).toList();
    }


    //        멤버 초대
    @Transactional
    public void addMember(AuthUser authUser,Long workSpaceId, String userEmail, MemberRole role) {
        WorkSpace workSpace = validateWorkSpace(authUser,workSpaceId);
        User invitedUser  = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("이메일가 존재하지 않습니다."));

        // 멤버 추가
        WorkspaceMember member = new WorkspaceMember(workSpace, invitedUser, role);
        workspaceMemberRepository.save(member);
    }

    //    삭제
    @Transactional
    public void deleteWorkSpace(AuthUser authUser, Long workSpaceId) {
        WorkSpace workSpace = validateWorkSpace(authUser, workSpaceId);
        workSpaceRepository.delete(workSpace);
    }

    //        유저,워크스페이스,권한 검증
    public WorkSpace validateWorkSpace(AuthUser authUser, Long workSpaceId) {
        User loggedInUser = userRepository.findById(authUser.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 없습니다."));
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));

//        workspaceMember에 userId.getEmail()이 존재 하지 않으면 권한이 없게 검증 처리
        List<WorkspaceMember> workspaceMember = workspaceMemberRepository.findByUserEmailAndWorkSpace_WorkspaceId(loggedInUser.getEmail(),workSpaceId);
        if (workspaceMember.stream().noneMatch(member -> member.getUser().getEmail().equals(loggedInUser.getEmail()))) {
            throw new IllegalArgumentException("권한이 없습니다");
        }
        return workSpace;
    }
}

