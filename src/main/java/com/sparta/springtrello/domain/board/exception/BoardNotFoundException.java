package com.sparta.springtrello.domain.board.exception;

import com.sparta.springtrello.domain.advice.exception.NotFoundException;

public class BoardNotFoundException extends NotFoundException {
  public BoardNotFoundException(String message) {
    super(message);
  }
}
