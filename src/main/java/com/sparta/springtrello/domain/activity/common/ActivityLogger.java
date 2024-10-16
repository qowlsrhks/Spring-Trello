package com.sparta.springtrello.domain.activity.common;

import com.sparta.springtrello.domain.activity.entity.ActivityType;
import com.sparta.springtrello.domain.activity.service.ActivityService;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.checklist.entity.ChecklistItem;
import com.sparta.springtrello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ActivityLogger {
    private final ActivityService activityService;

    public void logCardCreated(Card card, User user) {
        activityService.logActivity(card, user, ActivityType.CARD_CREATED, user.getUsername()+"님이 카드를 생성했습니다.");
    }

    public void logCardMoved(Card card, User user, String fromList, String toList) {
        String details = String.format(user.getUsername()+"카드를 '%s' 에서 '%s'로 옮겼습니다.", fromList, toList);
        activityService.logActivity(card, user, ActivityType.CARD_MOVED, details);
    }

    public void logChecklistAdded(Card card, User user, String checklistName) {
        String details = String.format(user.getUsername()+"님이 체크리스트를 추가했습니다 : %s", checklistName);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ADDED, details);
    }

    public void logChecklistItemAdded(Card card, User user, String itemContent) {
        String details = String.format(user.getUsername()+"님이 체크리스트 항목을 추가했습니다: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_ADDED, details);
    }

    public void logChecklistItemUpdate(Card card, User user, String itemContent) {
        String details = String.format(user.getUsername()+"님이 체크리스트를 수정했습니다.: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_UPDATE, details);
    }

    public void logChecklistItemComplete(Card card, User user, ChecklistItem item) {
        String details = String.format(user.getUsername()+"님이 " + item.getContent() + " 을(를) 완료했습니다.");
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_COMPLETED, details);
    }

    public void logChecklistItemUnComplete(Card card, User user, ChecklistItem item) {
        String details = String.format(user.getUsername() + "님이 " + item.getContent() + " 을(를) 체크 취소했습니다.");
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_UNCOMPLETED, details);
    }

    public void logChecklistItemDeleted(Card card, User user) {
        String details = String.format(user.getUsername()+"님이 체크리스트 항목을 삭제했습니다.");
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_DELETED, details);
    }

    public void logDueDateChanged(Card card, User user, LocalDate newDueDate) {
        String details = String.format(user.getUsername()+"님이 날짜를 변경했습니다: %s", newDueDate.toString());
        activityService.logActivity(card, user, ActivityType.DUE_DATE_CHANGED, details);
    }

    public void logLabelAdded(Card card, User user, String labelName) {
        String details = String.format(user.getUsername()+"님이 라벨을 추가했습니다: %s", labelName);
        activityService.logActivity(card, user, ActivityType.LABEL_ADDED, details);
    }

    public void logLabelRemoved(Card card, User user, String labelName) {
        String details = String.format(user.getUsername()+"님이 라벨을 삭제했습니다.: %s", labelName);
        activityService.logActivity(card, user, ActivityType.LABEL_REMOVED, details);
    }
}
