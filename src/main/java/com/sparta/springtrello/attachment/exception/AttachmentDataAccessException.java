package com.sparta.springtrello.attachment.exception;

import org.springframework.dao.DataAccessException;

public class AttachmentDataAccessException extends DataAccessException {
    public AttachmentDataAccessException(String s) {
        super(s);
    }
}
