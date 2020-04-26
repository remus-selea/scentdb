package com.github.remusselea.scentdb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageFileNotFoundException extends RuntimeException {

  public ImageFileNotFoundException(String message) {
    super(message);
  }

  public ImageFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}