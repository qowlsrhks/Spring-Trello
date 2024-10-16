package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.repository.WorkSpaceRepository;
import com.sparta.springtrello.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final UserRepository userRepository;

//    생성
    @Transactional
    public BoardResponseDto createBoard(Long userId, Long workSpaceId, BoardRequestDto boardRequestDto) {
        userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));

        Board board = new Board(boardRequestDto);
        board.setWorkSpace(workSpace);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }
//    단건조회
    public BoardResponseDto getBoard(Long userId,Long workSpaceId, Long boardId) {
        validateWorkSpace(userId,workSpaceId,boardId);
        return new BoardResponseDto(boardRepository.findByBoardId(boardId));
    }
//    다건
    public List<BoardResponseDto> getBoards(Long userId,Long workSpaceId) {
        userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
        workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));
        List<Board> boards = boardRepository.findAll();
        return boards.stream().map(BoardResponseDto::new).toList();
    }
//    수정
    @Transactional
    public BoardResponseDto updateBoard(Long userId,Long workSpaceId,Long boardId, BoardRequestDto boardRequestDto) {
        validateWorkSpace(userId,workSpaceId,boardId);
        Board board = new Board();
        board.update(boardRequestDto);
        return new BoardResponseDto(board);
    }
//    삭제
    @Transactional
    public void deleteBoard(Long userId,Long workSpaceId,Long boardId) {
        validateWorkSpace(userId,workSpaceId,boardId);
        boardRepository.delete(boardRepository.findByBoardId(boardId));
    }

//    검증
    public void validateWorkSpace(Long userId,Long workSpaceId,Long boardId) {
        userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
        workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));

        boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 보드입니다.")
        );
    }
}
