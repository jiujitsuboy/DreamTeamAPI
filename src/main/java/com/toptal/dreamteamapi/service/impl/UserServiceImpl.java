package com.toptal.dreamteamapi.service.impl;

import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.entity.UserTokenEntity;
import com.toptal.dreamteamapi.exception.GenericAlreadyExistsException;
import com.toptal.dreamteamapi.exception.InvalidRefreshTokenException;
import com.toptal.dreamteamapi.model.RefreshToken;
import com.toptal.dreamteamapi.model.SignedInUser;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.repository.UserRepository;
import com.toptal.dreamteamapi.repository.UserTokenRepository;
import com.toptal.dreamteamapi.security.JwtManager;
import com.toptal.dreamteamapi.service.TeamService;
import com.toptal.dreamteamapi.service.UserService;
import com.toptal.dreamteamapi.service.Util;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final TeamService teamService;
  private final UserRepository userRepository;
  private final UserTokenRepository userTokenRepository;
  private final PasswordEncoder bCryptPasswordEncoder;
  private final JwtManager tokenManager;

  public UserServiceImpl(TeamService teamService, UserRepository userRepository, UserTokenRepository userTokenRepository,
      PasswordEncoder bCryptPasswordEncoder,
      JwtManager tokenManager) {
    this.teamService = teamService;
    this.userRepository = userRepository;
    this.userTokenRepository = userTokenRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.tokenManager = tokenManager;
  }

  @Override
  public UserEntity signUp(User user) {
    UserEntity userEntity = createUser(user);
    teamService.createTeamForUser(userEntity);
    return userEntity;
  }

  @Override
  @Transactional
  public SignedInUser signUser(String username, String password) {

    if (Strings.isBlank(username)) {
      throw new UsernameNotFoundException("Invalid user.");
    }
    if (Strings.isBlank(password)) {
      throw new UsernameNotFoundException("Invalid password.");
    }
    final String uname = username.trim();
    Optional<UserEntity> oUserEntity = findUserByUserName(uname);
    UserEntity userEntity = oUserEntity.orElseThrow(
        () -> new InsufficientAuthenticationException("Unauthorized."));

    if (!bCryptPasswordEncoder.matches(password, userEntity.getPassword())) {
      throw new InsufficientAuthenticationException("Unauthorized.");
    }
    return getSignedInUser(userEntity);
  }

  @Override
  public void removeRefreshToken(RefreshToken refreshToken) {
    userTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
        .ifPresentOrElse(userTokenRepository::delete, () -> {
          throw new InvalidRefreshTokenException("Invalid token.");
        });
  }

  @Override
  public Optional<SignedInUser> getAccessToken(RefreshToken refreshToken) {
    // You may add an additional validation for time that would remove/invalidate the refresh token
    return userTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
        .map(ut -> {
          SignedInUser signedInUser = createSignedInUser(ut.getUser());
          signedInUser.setRefreshToken(refreshToken.getRefreshToken());
          return Optional.of(signedInUser);
        })
        .orElseThrow(() -> new InvalidRefreshTokenException("Invalid token."));
  }

  @Override
  public Optional<UserEntity> findUserByUserName(String username) {
    return userRepository.findByUsername(username);
  }

  @Transactional
  public SignedInUser getSignedInUser(UserEntity userEntity) {
    userTokenRepository.deleteByUserId(userEntity.getId());
    return createSignedUserWithRefreshToken(userEntity);
  }

  @Transactional
  protected UserEntity createUser(User user) {
    Integer count = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
    if (count > 0) {
      throw new GenericAlreadyExistsException("Use different username and email.");
    }
    UserEntity userEntity = (UserEntity) Util.toEntity(user);
    userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(userEntity);
    return userEntity;
  }

  private SignedInUser createSignedUserWithRefreshToken(UserEntity userEntity) {
    SignedInUser signedInUser = createSignedInUser(userEntity);
    signedInUser.setRefreshToken(createRefreshToken(userEntity));
    return signedInUser;
  }

  private SignedInUser createSignedInUser(UserEntity userEntity) {
    String token = tokenManager.create(org.springframework.security.core.userdetails.User.builder()
        .username(userEntity.getUsername())
        .password(userEntity.getPassword())
        .authorities(Objects.nonNull(userEntity.getRole()) ? userEntity.getRole().name() : "")
        .build());

    SignedInUser signedInUser = new SignedInUser();
    signedInUser.setUserId(userEntity.getId());
    signedInUser.setUserName(userEntity.getUsername());
    signedInUser.setAccessToken(token);

    return signedInUser;
  }

  private String createRefreshToken(UserEntity user) {
    String token = RandomHolder.randomKey(128);
    UserTokenEntity userTokenEntity = new UserTokenEntity();
    userTokenEntity.setRefreshToken(token);
    userTokenEntity.setUser(user);
    userTokenRepository.save(userTokenEntity);
    return token;
  }

  /**
   * Generate secure randoms Strings
   */
  private static class RandomHolder {

    static final Random random = new SecureRandom();

    public static String randomKey(int length) {
      return String.format("%" + length + "s", new BigInteger(length * 5/*base 32,2^5*/, random)
          .toString(32)).replace('\u0020', '0');
    }
  }
}
