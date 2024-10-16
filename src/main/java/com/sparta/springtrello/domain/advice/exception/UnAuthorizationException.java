package com.sparta.springtrello.domain.advice.exception;

public class UnAuthorizationException extends RuntimeException {
    public UnAuthorizationException(String s) {
        super(s);
    }
}
