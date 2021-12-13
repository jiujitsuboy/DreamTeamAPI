package com.toptal.dreamteamapi.exception;

public class NoSuchPlayerException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NoSuchPlayerException(final String message) {
    super(message);
  }
}
