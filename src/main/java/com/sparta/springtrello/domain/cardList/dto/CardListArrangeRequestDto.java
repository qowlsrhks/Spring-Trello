package com.sparta.springtrello.domain.cardList.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardListArrangeRequestDto {
    // listId : 위치를 이동할 list의 id
    // prevListId : 이동 시킨 list의 앞에 위치한 list의 id
    // prevListId 미전달 시 이동시킨 list는 맨 앞에 위치하게 됨.
    private Long prevListId;

    @NotNull
    private Long listId;
}
