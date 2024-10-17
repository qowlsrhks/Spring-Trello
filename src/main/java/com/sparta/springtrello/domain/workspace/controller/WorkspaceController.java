package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.common.Auth;
import com.sparta.springtrello.domain.common.AuthUser;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceMemberRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceResponseDto;
import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.service.WorkSpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkSpaceService workSpaceService;

    //    생성
    @PostMapping
    public ResponseEntity<WorkSpaceResponseDto> createWorkSpace(@Auth AuthUser authUser,@Valid @RequestBody WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.createWorkSpace(authUser,workSpaceRequestDto));
    }

//    단건조회
    @GetMapping("/{wsId}")
    public ResponseEntity<WorkSpaceResponseDto> getWorkSpace(@Auth AuthUser authUser, @PathVariable("wsId")Long workSpaceId) {
        return ResponseEntity.ok(workSpaceService.getWorkSpace(authUser,workSpaceId));
    }
//    다건조회
    @GetMapping("/workspacesList")
    public ResponseEntity<List<WorkSpaceMemberRequestDto>> getWorkSpaces(@Auth AuthUser authUser) {
        return ResponseEntity.ok(workSpaceService.getWorkSpaces(authUser));
    }
//    수정
    @PutMapping("/{wsId}")
    public ResponseEntity<WorkSpaceResponseDto> updateWorkSpace(@Auth AuthUser authUser,@PathVariable("wsId") Long workSpaceId,@Valid @RequestBody WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.updateWorkSpace(authUser,workSpaceId,workSpaceRequestDto));
    }
//    멤버 초대
    @PostMapping("/{wsId}/members")
    public ResponseEntity<String> addMember(@Auth AuthUser authUser,@PathVariable Long wsId, @RequestParam String userEmail, @RequestParam MemberRole role) {
        workSpaceService.addMember(authUser,wsId, userEmail, role);
        return ResponseEntity.ok("멤버가 추가되었습니다.");
    }

//    삭제
    @DeleteMapping("/{wsId}")
    public ResponseEntity<String> deleteWorkSpace(@Auth AuthUser authUser,@PathVariable("wsId") Long workSpaceId) {
        workSpaceService.deleteWorkSpace(authUser,workSpaceId);
        return ResponseEntity.ok("삭제되었습니다.");
    }

}
