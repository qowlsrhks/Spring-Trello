package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceResponseDto;
import com.sparta.springtrello.domain.workspace.service.WorkSpaceService;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@NoArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private WorkSpaceService workSpaceService;

    //    생성
    @PostMapping("/")
    public ResponseEntity<WorkSpaceResponseDto> createWorkSpace(WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.createWorkSpace(workSpaceRequestDto));
    }

//    단건조회
    @GetMapping("/{workSpaceId}")
    public ResponseEntity<WorkSpaceResponseDto> getWorkSpace(@PathVariable("workSpaceId")Long workSpaceId) {
        return ResponseEntity.ok(workSpaceService.getWorkSpace(workSpaceId));
    }
//    다건조회
    @GetMapping("/")
    public ResponseEntity<List<WorkSpaceResponseDto>> getWorkSpaces() {
        return ResponseEntity.ok(workSpaceService.getWorkSpaces());
    }
//    수정
    @PutMapping("/")
    public ResponseEntity<WorkSpaceResponseDto> updateWorkSpace(WorkSpaceRequestDto workSpaceRequestDto) {
        return ResponseEntity.ok(workSpaceService.updateWorkSpace(workSpaceRequestDto));
    }
//    삭제
    @DeleteMapping("/{workSpaceId}")
    public ResponseEntity<String> deleteWorkSpace(@PathVariable("workSpaceId") Long workSpaceId) {
        workSpaceService.deleteWorkSpace(workSpaceId);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
