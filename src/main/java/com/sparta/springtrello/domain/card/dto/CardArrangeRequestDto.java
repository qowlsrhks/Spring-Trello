package com.sparta.springtrello.domain.card.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardArrangeRequestDto {
    // cardId : 위치를 옮길 카드의 id -> 리퀘스트 바디에 필수로 필요한 값
    // toListId : 위치 변경 후 카드가 위치할 list의 id
    // prevCardId : 위치 변경 후 이동한 카드의 앞 순서에 위치한 카드의 id
    // prevCardId 값을 requestBody에 전달하지 않을 경우, 카드는 리스트의 맨 앞에 위치하게 됨.

    @NotNull
    private Long toListId;
    @NotNull
    private Long cardId;

    private Long prevCardId;
}
