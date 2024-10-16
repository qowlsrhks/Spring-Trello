package com.sparta.springtrello.domain.card.exception;

import com.sparta.springtrello.domain.advice.exception.NotFoundException;

public class CardNotFoundException extends NotFoundException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
