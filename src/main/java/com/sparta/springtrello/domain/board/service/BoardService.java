package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.repository.WorkSpaceRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkSpaceRepository workSpaceRepository;

//    생성
    @Transactional
    public BoardResponseDto createBoard(Long workSpaceId, BoardRequestDto boardRequestDto) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));
        Board board = new Board(boardRequestDto);
        board.setWorkSpace(workSpace);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }
//    단건조회
    public BoardResponseDto getBoard(Long workSpaceId, Long boardId) {
        workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 보드입니다.")
        );

        return new BoardResponseDto(board);
    }
//    다건
    public List<BoardResponseDto> getBoards(Long workSpaceId) {
        workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));
        List<Board> boards = boardRepository.findAll();
        return boards.stream().map(BoardResponseDto::new).toList();
    }
//    수정
    @Transactional
    public BoardResponseDto updateBoard(Long workSpaceId,Long boardId, BoardRequestDto boardRequestDto) {
        workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 보드입니다.")
        );
        board.update(boardRequestDto.getBoardTitle(), boardRequestDto.getBoardDescription(), boardRequestDto.getModifiedAt());

        return new BoardResponseDto(board);
    }
//    삭제
    @Transactional
    public void deleteBoard(Long workSpaceId,Long boardId) {
        workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 보드입니다.")
        );

        boardRepository.delete(board);
    }
}
