package com.sparta.springtrello.domain.cardList.exception;

import com.sparta.springtrello.domain.advice.exception.NotFoundException;

public class CardListNotFoundException extends NotFoundException {
    public CardListNotFoundException(String message) {
        super(message);
    }
}
