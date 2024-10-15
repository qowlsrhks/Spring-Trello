package com.sparta.springtrello.domain.workspace.service;

import com.sparta.springtrello.domain.workspace.dto.WorkSpaceRequestDto;
import com.sparta.springtrello.domain.workspace.dto.WorkSpaceResponseDto;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.repository.WorkSpaceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;
    //    생성
    @Transactional
    public WorkSpaceResponseDto createWorkSpace(WorkSpaceRequestDto workSpaceRequestDto) {
        WorkSpace workSpace = new WorkSpace(workSpaceRequestDto);
        workSpaceRepository.save(workSpace);
        return new WorkSpaceResponseDto(workSpace);

    }
//    수정
    public WorkSpaceResponseDto updateWorkSpace(Long workSpaceId,WorkSpaceRequestDto workSpaceRequestDto) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        workSpace.update(workSpaceRequestDto);
        workSpaceRepository.save(workSpace);
        return new WorkSpaceResponseDto(workSpace);
    }

//    단건조회
    public WorkSpaceResponseDto getWorkSpace(Long workSpaceId) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        return new WorkSpaceResponseDto(workSpace);

    }
//    다건조회
    public List<WorkSpaceResponseDto> getWorkSpaces() {
        List<WorkSpace> workSpaces = workSpaceRepository.findAll();
        return workSpaces.stream().map(WorkSpaceResponseDto::new).toList();
    }

//    삭제
    @Transactional
    public void deleteWorkSpace(Long workSpaceId) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        workSpaceRepository.delete(workSpace);
    }
}
