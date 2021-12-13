package com.toptal.dreamteamapi.exception;

public class InsufficientTeamBudgedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InsufficientTeamBudgedException(final String message) {
    super(message);
  }
}
