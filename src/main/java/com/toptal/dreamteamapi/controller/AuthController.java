package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.toptal.dreamteamapi.entity.UserEntity;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  private final UserService service;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserService service, PasswordEncoder passwordEncoder) {
    this.service = service;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(
      value = "/auth/token/refresh",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<SignedInUser> getAccessToken(@Valid @RequestBody(required = false) RefreshToken refreshToken) {
    return ok(service.getAccessToken(refreshToken).get());
  }

  @PostMapping(
      value = "/auth/token",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<SignedInUser> signIn(@Valid @RequestBody(required = false) SignInReq signInReq) {

    UserEntity userEntity = service.findUserByUsername(signInReq.getUsername());
    if (passwordEncoder.matches(signInReq.getPassword(), userEntity.getPassword())) {
      return ok(service.getSignedInUser(userEntity));
    } else {
      throw new InsufficientAuthenticationException("Unauthorized.");
    }
  }

  @DeleteMapping(
      value = "/auth/token",
      consumes = {"application/json"}
  )
  public ResponseEntity<Void> signOut(@Valid @RequestBody(required = false) RefreshToken refreshToken) {
    // We are using removeToken API for signout.
    // Ideally you would like to get tgit she user ID from Logged in user's request
    // and remove the refresh token based on retrieved user id from request.
    service.removeRefreshToken(refreshToken);
    return accepted().build();
  }

  @PostMapping(
      value = "/users",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<SignedInUser> signUp(@Valid @RequestBody(required = false) User user) {
    return status(HttpStatus.CREATED).body(service.createUser(user).get());
  }

}
