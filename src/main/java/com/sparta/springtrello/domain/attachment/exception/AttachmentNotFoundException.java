package com.sparta.springtrello.domain.attachment.exception;

import com.sparta.springtrello.domain.advice.exception.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException(String s) {
        super(s);
    }
}
