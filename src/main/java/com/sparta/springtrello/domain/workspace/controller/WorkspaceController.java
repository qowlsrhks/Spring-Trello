package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceResponseDto;
import com.sparta.springtrello.domain.workspace.service.WorkSpaceService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/")
    public ResponseEntity<WorkSpaceResponseDto> createWorkSpace(@RequestBody WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.createWorkSpace(workSpaceRequestDto));
    }

//    단건조회
    @GetMapping("/{wsId}")
    public ResponseEntity<WorkSpaceResponseDto> getWorkSpace(@PathVariable("wsId")Long workSpaceId) {
        return ResponseEntity.ok(workSpaceService.getWorkSpace(workSpaceId));
    }
//    다건조회
    @GetMapping("/workspacesList")
    public ResponseEntity<List<WorkSpaceResponseDto>> getWorkSpaces() {
        return ResponseEntity.ok(workSpaceService.getWorkSpaces());
    }
//    수정
    @PutMapping("/{wsId}")
    public ResponseEntity<WorkSpaceResponseDto> updateWorkSpace(@PathVariable("wsId") Long workSpaceId, @RequestBody WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.updateWorkSpace(workSpaceId,workSpaceRequestDto));
    }
//    삭제
    @DeleteMapping("/{wsId}")
    public ResponseEntity<String> deleteWorkSpace(@PathVariable("wsId") Long workSpaceId) {
        workSpaceService.deleteWorkSpace(workSpaceId);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
