package com.sparta.springtrello.domain.activity.entity;

import com.sparta.springtrello.domain.activity.service.ActivityService;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.user.entity.User;
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

    public void logCardArchived(Card card, User user) {
        activityService.logActivity(card, user, ActivityType.CARD_ARCHIVED, user.getUsername()+"님이 카드를 보관했습니다.");
    }

    public void logCardRestored(Card card, User user) {
        activityService.logActivity(card, user, ActivityType.CARD_RESTORED, user.getUsername()+"님이 카드를 복원했습니다.");
    }

    public void logCommentAdded(Card card, User user, String commentPreview) {
        String details = String.format(user.getUsername()+"님의 코멘트 %s", commentPreview);
        activityService.logActivity(card, user, ActivityType.COMMENT_ADDED, details);
    }

    public void logCommentEdited(Card card, User user, String commentPreview) {
        String details = String.format(user.getUsername()+"님이 코멘트를 수정했습니다.: %s", commentPreview);
        activityService.logActivity(card, user, ActivityType.COMMENT_EDITED, details);
    }

    public void logCommentDeleted(Card card, User user) {
        activityService.logActivity(card, user, ActivityType.COMMENT_DELETED, user.getUsername()+"님이 코멘트를 삭제했습니다.");
    }

    public void logChecklistAdded(Card card, User user, String checklistName) {
        String details = String.format(user.getUsername()+"님이 체크리스트를 추가했습니다 : %s", checklistName);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ADDED, details);
    }

    public void logChecklistItemAdded(Card card, User user, String itemContent) {
        String details = String.format(user.getUsername()+"님이 체크리스트 항목을 추가했습니다: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_ADDED, details);
    }

    public void logChecklistItemCompleted(Card card, User user, String itemContent) {
        String details = String.format("체크리스트가 완료되었습니다.: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_COMPLETED, details);
    }

    public void logChecklistItemDeleted(Card card, User user, String itemContent) {
        String details = String.format(user.getUsername()+"님이 체크리스트 항목을 삭제했습니다.: %s", itemContent);
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
