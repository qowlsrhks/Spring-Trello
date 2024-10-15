package com.sparta.springtrello.domain.comment.activity.entity;

import com.sparta.springtrello.domain.comment.activity.service.ActivityService;
import com.sparta.springtrello.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.smartcardio.Card;
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
        String details = String.format("Checklist added: %s", checklistName);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ADDED, details);
    }

    public void logChecklistItemAdded(Card card, User user, String itemContent) {
        String details = String.format("Checklist item added: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_ADDED, details);
    }

    public void logChecklistItemCompleted(Card card, User user, String itemContent) {
        String details = String.format("Checklist item completed: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_COMPLETED, details);
    }

    public void logChecklistItemUncompleted(Card card, User user, String itemContent) {
        String details = String.format("Checklist item uncompleted: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_UNCOMPLETED, details);
    }

    public void logChecklistItemDeleted(Card card, User user, String itemContent) {
        String details = String.format("Checklist item deleted: %s", itemContent);
        activityService.logActivity(card, user, ActivityType.CHECKLIST_ITEM_DELETED, details);
    }

    public void logMemberAdded(Card card, User adder, User addedMember) {
        String details = String.format("Member added to card: %s", addedMember.getName());
        activityService.logActivity(card, adder, ActivityType.MEMBER_ADDED, details);
    }

    public void logMemberRemoved(Card card, User remover, User removedMember) {
        String details = String.format("Member removed from card: %s", removedMember.getName());
        activityService.logActivity(card, remover, ActivityType.MEMBER_REMOVED, details);
    }

    public void logDueDateChanged(Card card, User user, LocalDate newDueDate) {
        String details = String.format("Due date changed to: %s", newDueDate.toString());
        activityService.logActivity(card, user, ActivityType.DUE_DATE_CHANGED, details);
    }

    public void logLabelAdded(Card card, User user, String labelName) {
        String details = String.format("Label added: %s", labelName);
        activityService.logActivity(card, user, ActivityType.LABEL_ADDED, details);
    }

    public void logLabelRemoved(Card card, User user, String labelName) {
        String details = String.format("Label removed: %s", labelName);
        activityService.logActivity(card, user, ActivityType.LABEL_REMOVED, details);
    }
}
