package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class BoardService {

    private BoardRepository boardRepository;
//    private UserRepository userRepository;

//    생성
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {
//        User user = userRepository.findById(userId).orElseThrow(()
//                -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Board board = new Board(boardRequestDto);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }
//    단건조회
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 보드입니다.")
        );
        return new BoardResponseDto(board);
    }
//    다건
    public List<BoardResponseDto> getBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream().map(BoardResponseDto::new).toList();
    }
//    수정
    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto) {
        Board board = new Board(boardRequestDto);
        board.update(boardRequestDto.getBoardTitle(), boardRequestDto.getBoardDescription(), boardRequestDto.getModifiedAt());
        return new BoardResponseDto(board);
    }
//    삭제
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 보드입니다.")
        );
        boardRepository.delete(board);
    }
}
