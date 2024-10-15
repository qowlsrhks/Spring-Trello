package com.sparta.springtrello.domain.attachment.exception;

import com.sparta.springtrello.domain.advice.exception.BadRequestException;

public class FileBadRequestException extends BadRequestException {
    public FileBadRequestException(String message) {
        super(message);
    }
}
