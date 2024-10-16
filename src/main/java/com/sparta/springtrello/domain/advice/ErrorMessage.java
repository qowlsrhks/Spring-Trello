package com.sparta.springtrello.domain.advice;

import lombok.Getter;

@Getter
public class ErrorMessage {
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }
}
