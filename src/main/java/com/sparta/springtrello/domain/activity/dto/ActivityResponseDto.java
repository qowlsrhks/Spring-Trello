package com.sparta.springtrello.domain.activity.dto;

import com.sparta.springtrello.domain.activity.entity.Activity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class ActivityResponseDto {
    private Long id;
    private String activity;

    public ActivityResponseDto(Activity activity) {
        this.id = activity.getId();
        this.activity = activity.getContents();
    }
}
