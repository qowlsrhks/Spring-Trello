package com.sparta.springtrello.domain.activity.service;

import com.sparta.springtrello.domain.activity.entity.Activity;
import com.sparta.springtrello.domain.activity.entity.ActivityType;
import com.sparta.springtrello.domain.activity.repository.ActivityRepository;
import com.sparta.springtrello.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.smartcardio.Card;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ActivityService {
    private final ActivityRepository activityRepository;

    public Activity logActivity(Card card, User user, ActivityType type, String details) {
        Activity activity = new Activity(card, user, type, details, null);
        return activityRepository.save(activity);
    }

    public List<Activity> getActivitiesForCard(Card card) {
        return activityRepository.findByCardOrderByCreatedAtDesc(card);
    }

}
