package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceResponseDto;
import com.sparta.springtrello.domain.workspace.service.WorkSpaceService;
import com.sparta.springtrello.user.entity.User;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkSpaceService workSpaceService;

    //    생성
    @PostMapping("/{userId}")
    public ResponseEntity<WorkSpaceResponseDto> createWorkSpace(@PathVariable("userId") Long userId, @Valid @RequestBody WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.createWorkSpace(userId,workSpaceRequestDto));
    }

//    단건조회
    @GetMapping("/{userId}/{wsId}")
    public ResponseEntity<WorkSpaceResponseDto> getWorkSpace(@PathVariable("userId") Long userId, @PathVariable("wsId")Long workSpaceId) {
        return ResponseEntity.ok(workSpaceService.getWorkSpace(userId,workSpaceId));
    }
//    다건조회
    @GetMapping("/{userId}/workspacesList")
    public ResponseEntity<List<WorkSpaceResponseDto>> getWorkSpaces(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(workSpaceService.getWorkSpaces(userId));
    }
//    수정
    @PutMapping("/{userId}/{wsId}")
    public ResponseEntity<WorkSpaceResponseDto> updateWorkSpace(@PathVariable("userId") Long userId,@PathVariable("wsId") Long workSpaceId,@Valid @RequestBody WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.updateWorkSpace(userId,workSpaceId,workSpaceRequestDto));
    }
//    삭제
    @DeleteMapping("/{userId}/{wsId}")
    public ResponseEntity<String> deleteWorkSpace(@PathVariable("userId") Long userId,@PathVariable("wsId") Long workSpaceId) {
        workSpaceService.deleteWorkSpace(userId,workSpaceId);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
