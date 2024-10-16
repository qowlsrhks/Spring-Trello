package com.sparta.springtrello.domain.checklist.dto.response;

import com.sparta.springtrello.domain.checklist.entity.Checklist;
import com.sparta.springtrello.domain.checklist.entity.ChecklistItem;
import com.sparta.springtrello.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class ChecklistResponseDto {
    private Long id;
    private String contents;
    private List<ChecklistItem> checkListItems;
    private String username;

    public ChecklistResponseDto(Checklist checklist, User user) {
        this.id = checklist.getId();
        this.contents = checklist.getTitle();
        this.checkListItems = checklist.getItems();
        this.username = user.getUsername();
    }

    public ChecklistResponseDto(String title, User user) {
        this.contents = title;
        this.username = user.getUsername();
    }
}
