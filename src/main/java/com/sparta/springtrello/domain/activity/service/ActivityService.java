package com.sparta.springtrello.domain.activity.service;

import com.sparta.springtrello.domain.activity.dto.ActivityResponseDto;
import com.sparta.springtrello.domain.activity.dto.CardActivityResponseDto;
import com.sparta.springtrello.domain.activity.entity.Activity;
import com.sparta.springtrello.domain.activity.entity.ActivityType;
import com.sparta.springtrello.domain.activity.repository.ActivityRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.checklist.entity.Checklist;
import com.sparta.springtrello.domain.checklist.entity.ChecklistItem;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    public Activity logActivity(Card card, User user, ActivityType type, String details) {
        Activity activity = new Activity(card, user, type, details, null);
        return activityRepository.save(activity);
    }

    public CardActivityResponseDto getActivitiesForCard(Long cardId, String email) {
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("접근 권한이 없습니다."));
        List<CommentResponseDto> comment = commentRepository.findByCardOrderByModifiedAtDesc(card).stream().map(CommentResponseDto::new).toList();
        List<ActivityResponseDto> activity = activityRepository.findByCardOrderByCreatedAtDesc(card).stream().map(ActivityResponseDto::new).toList();
        return new CardActivityResponseDto(comment, activity);
    }

    public Activity logActivity(Checklist checklist, User user, ActivityType activityType, String details) {
        Activity activity = new Activity(checklist, user, activityType, details);
        return activityRepository.save(activity);
    }

    public Activity logActivity(ChecklistItem checklistItem, User user, ActivityType activityType, String details) {
        Activity activity = new Activity(checklistItem, user, activityType, details);
        return activityRepository.save(activity);
    }

    public Activity logActivity(ChecklistItem checklistItem, User user, ActivityType activityType) {
        Activity activity = new Activity(checklistItem, user, activityType);
        return activityRepository.save(activity);
    }
}
