package com.sparta.springtrello.domain.workspace.service;


import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import com.sparta.springtrello.domain.user.entity.Role;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceResponseDto;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.repository.WorkSpaceRepository;

import com.sparta.springtrello.domain.user.entity.Role;

import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public WorkSpaceResponseDto createWorkSpace(Long userId, WorkSpaceRequestDto workSpaceRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
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
    public WorkSpaceResponseDto updateWorkSpace(Long userId,Long workSpaceId,WorkSpaceRequestDto workSpaceRequestDto) {
        userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        workSpace.update(workSpaceRequestDto);
        workSpaceRepository.save(workSpace);
        return new WorkSpaceResponseDto(workSpace);
    }

//    단건조회
    public WorkSpaceResponseDto getWorkSpace(Long userId,Long workSpaceId) {
        userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));

        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        return new WorkSpaceResponseDto(workSpace);

    }
//    다건조회
    public List<WorkSpaceResponseDto> getWorkSpaces(Long userId) {
        userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));

        List<WorkSpace> workSpaces = workSpaceRepository.findAll();
        return workSpaces.stream().map(WorkSpaceResponseDto::new).toList();
    }


    @Transactional
    public void addMember(Long workSpaceId, Long userId, MemberRole role) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 멤버 추가
        WorkspaceMember member = new WorkspaceMember(workSpace, user, role);
        workspaceMemberRepository.save(member);
    }

//    삭제
    @Transactional
    public void deleteWorkSpace(Long userId,Long workSpaceId) {
        userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        workSpaceRepository.delete(workSpace);
    }
}
