package com.github.remusselea.scentdb.exception;

public class ScentdbBusinessUncheckedException extends RuntimeException {

  public ScentdbBusinessUncheckedException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public ScentdbBusinessUncheckedException(String errorMessage) {
    super(errorMessage);
  }
}
