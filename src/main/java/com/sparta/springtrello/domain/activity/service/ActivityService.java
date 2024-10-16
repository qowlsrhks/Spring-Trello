package com.sparta.springtrello.domain.activity.service;

import com.sparta.springtrello.domain.activity.dto.ActivityResponseDto;
import com.sparta.springtrello.domain.activity.entity.Activity;
import com.sparta.springtrello.domain.activity.entity.ActivityType;
import com.sparta.springtrello.domain.activity.repository.ActivityRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.user.entity.User;
import com.sparta.springtrello.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public Activity logActivity(Card card, User user, ActivityType type, String details) {
        Activity activity = new Activity(card, user, type, details, null);
        return activityRepository.save(activity);
    }

    public List<ActivityResponseDto> getActivitiesForCard(Long cardId, String email) {
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("접근 권한이 없습니다."));
        return activityRepository.findByCardOrderByCreatedAtDesc(card).stream().map(ActivityResponseDto::new).toList();
    }

}
