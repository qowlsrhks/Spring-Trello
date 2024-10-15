package com.sparta.springtrello.attachment.exception;

import com.sparta.springtrello.advice.exception.BadRequestException;

public class FileBadRequestException extends BadRequestException {
    public FileBadRequestException(String message) {
        super(message);
    }
}
