package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.dto.ListResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.common.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import com.sparta.springtrello.domain.workspace.repository.WorkSpaceRepository;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;

//    생성
    @Transactional
    public BoardResponseDto createBoard(AuthUser authUser, Long workSpaceId, BoardRequestDto boardRequestDto) {
        User loggedInUser = userRepository.findById(authUser.getUserId()).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));

        List<WorkspaceMember> workspaceMember = workspaceMemberRepository.findByUserEmail(loggedInUser.getEmail());
        List<WorkspaceMember> getWorkspaceMemberRole = workspaceMemberRepository.findByRole(workspaceMember.get(0).getRole());

        if(getWorkspaceMemberRole.get(0).getRole() == MemberRole.READ_ONLY) {
            throw new IllegalArgumentException("읽기 전용 멤버는 보드 생성을 할수 없습니다.");
        }
//       자신이 속해 있는 워크스페이스에 보드 조회
        Board board = new Board(boardRequestDto);
        board.setWorkSpace(workSpace);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }
//    단건조회
    public ListResponseDto getBoard(AuthUser authUser,Long workSpaceId, Long boardId) {
        User loggedInUser = userRepository.findById(authUser.getUserId()).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));
//       자신이 속해 있는 워크스페이스에 보드 조회
        List<WorkspaceMember> getWorkspaceMemberEmail = workspaceMemberRepository.findByUserEmailAndWorkSpace_WorkspaceId(loggedInUser.getEmail(),workSpace.getWorkspaceId());
        List<Board> boards = boardRepository.findByWorkSpace_WorkspaceId(getWorkspaceMemberEmail.get(0).getWorkSpace().getWorkspaceId());


        if(boards.stream().noneMatch(board -> board.getBoardId().equals(boardId))){
            throw new IllegalArgumentException("보드가 존재하지 않습니다");
        }

        return new ListResponseDto(boardRepository.findByBoardId(boardId));
    }
//    다건조회
    public List<BoardResponseDto> getBoards(AuthUser authUser,Long workSpaceId) {
       User loggedInUser = userRepository.findById(authUser.getUserId()).orElseThrow(()->
                new IllegalArgumentException("해당 유저가 없습니다."));
       WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));
//       자신이 속해 있는 워크스페이스 조회
        List<WorkspaceMember> getWorkspaceMemberEmail = workspaceMemberRepository.findByUserEmailAndWorkSpace_WorkspaceId(loggedInUser.getEmail(),workSpace.getWorkspaceId());
        List<Board> boards = boardRepository.findByWorkSpace_WorkspaceId(getWorkspaceMemberEmail.get(0).getWorkSpace().getWorkspaceId());

        if (boards.isEmpty()) {
            throw new IllegalArgumentException("보드가 존재하지 않습니다.");
        }
        return boards.stream().map(BoardResponseDto::new).toList();
    }
//    수정
    @Transactional
    public BoardResponseDto updateBoard(AuthUser authUser,Long workSpaceId,Long boardId, BoardRequestDto boardRequestDto) {
        validateWorkSpace(authUser,workSpaceId);
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("보드가 존재하지 않습니다."));
        board.update(boardRequestDto);
        return new BoardResponseDto(board);
    }

//    삭제
    @Transactional
    public void deleteBoard(AuthUser authUser,Long workSpaceId,Long boardId) {
        validateWorkSpace(authUser,workSpaceId);
        boardRepository.delete(boardRepository.findByBoardId(boardId));
    }

//    검증
    public WorkSpace validateWorkSpace(AuthUser authUser,Long workSpaceId) {
       User loggedInUser = userRepository.findById(authUser.getUserId()).orElseThrow(() ->
               new IllegalArgumentException("존재하지 않는 사용자입니다."));
       WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));
        List<WorkspaceMember> workspaceMember = workspaceMemberRepository.findByUserEmail(loggedInUser.getEmail());
        List<WorkspaceMember> getWorkspaceMemberRole = workspaceMemberRepository.findByRole(workspaceMember.get(0).getRole());

        if(getWorkspaceMemberRole.get(0).getRole() == MemberRole.READ_ONLY) {
            throw new IllegalArgumentException("읽기 전용 멤버는 보드 생성을 할수 없습니다.");
        }
        return workSpace;
    }
}
