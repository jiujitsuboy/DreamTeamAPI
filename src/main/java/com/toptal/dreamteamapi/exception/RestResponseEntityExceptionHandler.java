package com.toptal.dreamteamapi.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

  @ExceptionHandler(GenericAlreadyExistsException.class)
  protected ResponseEntity<Object> genericAlreadyExistsException(
      GenericAlreadyExistsException ex, WebRequest request) {

      Map<String, Object> body = new LinkedHashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("message", ex.getMessage());

      return new ResponseEntity<>(body, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InsufficientTeamBudgedException.class)
  protected ResponseEntity<Object> insufficientTeamBudgedException(
      InsufficientTeamBudgedException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(InvalidRefreshTokenException.class)
  protected ResponseEntity<Object> invalidRefreshTokenException(
      InvalidRefreshTokenException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoSuchPlayerException.class)
  protected ResponseEntity<Object> noSuchPlayerException(
      NoSuchPlayerException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoSuchTeamException.class)
  protected ResponseEntity<Object> noSuchTeamException(
      NoSuchTeamException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  protected ResponseEntity<Object> insufficientAuthenticationException(
      InsufficientAuthenticationException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  protected ResponseEntity<Object> usernameNotFoundException(
      UsernameNotFoundException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }


}

