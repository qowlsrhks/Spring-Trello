package com.sparta.springtrello.attachment.exception;

public class FileEmptyException extends RuntimeException{
    public FileEmptyException(String message) {
        super(message);
    }
}
