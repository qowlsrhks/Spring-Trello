package com.sparta.springtrello.domain.checklist.repository;

import com.sparta.springtrello.domain.checklist.entity.Checklist;
import com.sparta.springtrello.domain.checklist.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByChecklistOrderByIdAsc(Checklist checklist);

}
