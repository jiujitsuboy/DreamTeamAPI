package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.toptal.dreamteamapi.exception.InvalidRefreshTokenException;
import com.toptal.dreamteamapi.hateoas.UserRepresentationModelAssembler;
import com.toptal.dreamteamapi.model.RefreshToken;
import com.toptal.dreamteamapi.model.SignInReq;
import com.toptal.dreamteamapi.model.SignedInUser;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.service.UserService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserService service;
  private final UserRepresentationModelAssembler userAssembler;

  public AuthController(UserService service, UserRepresentationModelAssembler userAssembler) {
    this.service = service;
    this.userAssembler = userAssembler;
  }

  @PostMapping(
      value = "/token/refresh",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<SignedInUser> getAccessToken(@Valid @RequestBody(required = false) RefreshToken refreshToken) {
    return ok(service.getAccessToken(refreshToken).get());
  }

  @PostMapping(
      value = "/token",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<?> signIn(@Valid @RequestBody(required = false) SignInReq signInReq) {
    try{
      return  status(HttpStatus.ACCEPTED).body(userAssembler.toModel(service.signUser(signInReq.getUserName(), signInReq.getPassword())));
    }
    catch(UsernameNotFoundException ex){
      return status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    catch(InsufficientAuthenticationException ex){
      return status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
  }

  @DeleteMapping(
      value = "/token",
      consumes = {"application/json"}
  )
  public ResponseEntity<Void> signOut(@Valid @RequestBody(required = false) RefreshToken refreshToken) {
    // We are using removeToken API for signout.
    // Ideally you would like to get tgit she user ID from Logged in user's request
    // and remove the refresh token based on retrieved user id from request.
    try {
      service.removeRefreshToken(refreshToken);
      return accepted().build();
    }
    catch(InvalidRefreshTokenException ex){
      return status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostMapping(
      value = "/users",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<User> signUp(@Valid @RequestBody(required = false) User user) {
    return status(HttpStatus.CREATED).body(userAssembler.toModel(service.signUp(user)));
  }

}
