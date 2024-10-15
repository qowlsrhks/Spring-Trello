package com.sparta.springtrello.attachment.exception;

import com.sparta.springtrello.advice.exception.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException(String s) {
        super(s);
    }
}
