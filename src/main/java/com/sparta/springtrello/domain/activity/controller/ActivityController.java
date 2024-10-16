package com.sparta.springtrello.domain.activity.controller;

import com.sparta.springtrello.domain.activity.dto.ActivityResponseDto;
import com.sparta.springtrello.domain.activity.service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/activity/{cardId}")
    ResponseEntity<List<ActivityResponseDto>> getActivity(@PathVariable Long cardId, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(activityService.getActivitiesForCard(cardId, email));
    }
}
