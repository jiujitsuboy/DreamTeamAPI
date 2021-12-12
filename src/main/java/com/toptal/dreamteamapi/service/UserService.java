package com.toptal.dreamteamapi.service;

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
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository repository;
  private final UserTokenRepository userTokenRepository;
  private final PasswordEncoder bCryptPasswordEncoder;
  private final JwtManager tokenManager;

  public UserService(UserRepository repository, UserTokenRepository userTokenRepository, PasswordEncoder bCryptPasswordEncoder,
      JwtManager tokenManager) {
    this.repository = repository;
    this.userTokenRepository = userTokenRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.tokenManager = tokenManager;
  }

  @Transactional
  public Optional<SignedInUser> createUser(User user) {
    Integer count = repository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
    if (count > 0) {
      throw new GenericAlreadyExistsException("Use different username and email.");
    }
    UserEntity userEntity = repository.save(toEntity(user));
    return Optional.of(createSignedUserWithRefreshToken(userEntity));
  }

  public UserEntity findUserByUsername(String username) {
    if (Strings.isBlank(username)) {
      throw new UsernameNotFoundException("Invalid user.");
    }
    final String uname = username.trim();
    Optional<UserEntity> oUserEntity = repository.findByUsername(uname);
    UserEntity userEntity = oUserEntity.orElseThrow(
        () -> new UsernameNotFoundException(String.format("Given user(%s) not found.", uname)));
    return userEntity;
  }

  public void removeRefreshToken(RefreshToken refreshToken) {
    userTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
        .ifPresentOrElse(userTokenRepository::delete, () -> {
          throw new InvalidRefreshTokenException("Invalid token.");
        });
  }

  @Transactional
  public SignedInUser getSignedInUser(UserEntity userEntity) {
    userTokenRepository.deleteByUserId(userEntity.getId());
    return createSignedUserWithRefreshToken(userEntity);
  }

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

  private UserEntity toEntity(User user) {
    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);
    userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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

  private static class RandomHolder {

    static final Random random = new SecureRandom();

    public static String randomKey(int length) {
      return String.format("%" + length + "s", new BigInteger(length * 5/*base 32,2^5*/, random)
          .toString(32)).replace('\u0020', '0');
    }
  }
}
