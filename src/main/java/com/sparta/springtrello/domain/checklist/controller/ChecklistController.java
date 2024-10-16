package com.sparta.springtrello.domain.checklist.controller;

import com.sparta.springtrello.domain.checklist.dto.request.ChecklistRequestDto;
import com.sparta.springtrello.domain.checklist.dto.response.ChecklistResponseDto;
import com.sparta.springtrello.domain.checklist.entity.Checklist;
import com.sparta.springtrello.domain.checklist.entity.ChecklistItem;
import com.sparta.springtrello.domain.checklist.service.ChecklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards/{cardId}/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @PostMapping
    public ResponseEntity<ChecklistResponseDto> createChecklist(
            @PathVariable Long cardId,
            @RequestBody ChecklistRequestDto checklistRequestDto,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(checklistService.createChecklist(cardId, checklistRequestDto, email));
    }

    @GetMapping
    public ResponseEntity<List<Checklist>> getChecklists(@PathVariable Long cardId) {
        List<Checklist> checklists = checklistService.getChecklistsByCard(cardId);
        return ResponseEntity.ok(checklists);
    }

    @PutMapping("/{checklistId}")
    public ResponseEntity<ChecklistResponseDto> updateChecklistName(
            @PathVariable Long checklistId,
            @RequestBody ChecklistRequestDto checklistRequestDto,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");

        return ResponseEntity.ok(checklistService.updateChecklistName(checklistId, checklistRequestDto, email));
    }

    @DeleteMapping("/{checklistId}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable Long checklistId,
                                                HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        checklistService.deleteChecklist(checklistId, email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{checklistId}/items")
    public ResponseEntity<ChecklistItem> addChecklistItem(
            @PathVariable Long checklistId,
            @RequestBody ChecklistRequestDto checklistRequestDto,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        ChecklistItem item = checklistService.addChecklistItem(checklistId, checklistRequestDto, email);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ChecklistResponseDto> updateChecklistItem(
            @PathVariable Long itemId,
            @RequestBody ChecklistRequestDto checklistRequestDto,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(checklistService.updateChecklistItem(itemId, checklistRequestDto, email));

    }

    @PatchMapping("/items/{itemId}/toggle")
    public ResponseEntity<ChecklistItem> toggleChecklistItem(@PathVariable Long itemId,
                                                             HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        ChecklistItem item = checklistService.toggleChecklistItem(itemId, email);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable Long itemId,
                                                    HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        checklistService.deleteChecklistItem(itemId, email);
        return ResponseEntity.noContent().build();
    }}
