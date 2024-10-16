package com.sparta.springtrello.domain.checklist.service;

import com.sparta.springtrello.domain.activity.common.ActivityLogger;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.checklist.dto.request.ChecklistRequestDto;
import com.sparta.springtrello.domain.checklist.dto.response.ChecklistResponseDto;
import com.sparta.springtrello.domain.checklist.entity.Checklist;
import com.sparta.springtrello.domain.checklist.entity.ChecklistItem;
import com.sparta.springtrello.domain.checklist.repository.ChecklistItemRepository;
import com.sparta.springtrello.domain.checklist.repository.ChecklistRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final CardRepository cardRepository;
    private final ActivityLogger activityLogger;
    private final UserRepository userRepository;

    @Transactional
    public ChecklistResponseDto createChecklist(Long cardId, ChecklistRequestDto checklistRequestDto, String email) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));

        Checklist checklist = new Checklist(checklistRequestDto.getTitle(), card);
        checklistRepository.save(checklist);
        activityLogger.logChecklistAdded(card, user, checklistRequestDto.getTitle());
        return new ChecklistResponseDto(checklist, user);
    }

    @Transactional(readOnly = true)
    public List<Checklist> getChecklistsByCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));

        return checklistRepository.findByCardOrderByIdAsc(card);
    }

    @Transactional
    public ChecklistResponseDto updateChecklistName(Long checklistId, ChecklistRequestDto checklistRequestDto, String email) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트를 찾을 수 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));

        checklist.updateName(checklistRequestDto.getTitle());

        return new ChecklistResponseDto(checklist, user);
    }

    @Transactional
    public Long deleteChecklist(Long checklistId, String email) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트를 찾을 수 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        checklistRepository.delete(checklist);
        return checklistId;
    }

    @Transactional
    public ChecklistItem addChecklistItem(Long checklistId, ChecklistRequestDto checklistRequestDto, String email) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트를 찾을 수 없습니다."));

        Card card = cardRepository.findById(checklist.getCard().getCardId()).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));

        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        ChecklistItem item = new ChecklistItem(checklistRequestDto.getTitle(), checklist);
        checklist.addItem(item);
        activityLogger.logChecklistItemAdded(card, user, checklistRequestDto.getTitle());
        return checklistItemRepository.save(item);
    }

    @Transactional
    public ChecklistResponseDto updateChecklistItem(Long itemId, ChecklistRequestDto checklistRequestDto, String email) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트 아이템을 찾을 수 없습니다."));
        Card card = cardRepository.findById(item.getChecklist().getCard().getCardId()).orElseThrow(()-> new IllegalArgumentException("카드를 찾을수 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        item.updateContent(checklistRequestDto.getTitle());
        activityLogger.logChecklistItemUpdate(card, user, checklistRequestDto.getTitle());
        return new ChecklistResponseDto(checklistRequestDto.getTitle(), user);
    }

    @Transactional
    public ChecklistItem toggleChecklistItem(Long itemId, String email) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트 아이템을 찾을 수 없습니다."));
        Card card = cardRepository.findById(item.getChecklist().getCard().getCardId()).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));

//        item.toggleCompleted();
        if(!item.isCompleted()) {
            item.setCompleted(true);
            activityLogger.logChecklistItemComplete(card, user, item);
        } else if(item.isCompleted()) {
            item.setCompleted(false);
            activityLogger.logChecklistItemUnComplete(card, user, item);
        }

        return item;
    }

    @Transactional
    public void deleteChecklistItem(Long itemId, String email) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트 아이템을 찾을 수 없습니다."));
        Card card = cardRepository.findById(item.getChecklist().getCard().getCardId()).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));

        item.getChecklist().removeItem(item);
        checklistItemRepository.delete(item);
        activityLogger.logChecklistItemDeleted(card, user);
    }
}
