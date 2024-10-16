package com.sparta.springtrello.domain.board.repository;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findByBoardId(Long boardId);

    List<Board> findBoardByWorkSpace(WorkSpace workSpace);
}
