package com.toptal.dreamteamapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private TeamService teamService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserTokenRepository userTokenRepository;
  @Mock
  private PasswordEncoder bCryptPasswordEncoder;
  @Mock
  private JwtManager tokenManager;
  @InjectMocks
  private UserService classUnderTest;

  @Test
  public void signUp() {
    final int ZERO_RECORD = 0;
    final String cypheredPassword = "{bcrypt}$2a$10$Pb8XsVlOE493g6y//MFQ1OkiVjYQBT4VdkUk7s0b0vNI7m7LBliRG";
    String username = "scott1";

    UUID userUUID = UUID.randomUUID();
    User user = new User();
    user.setId(userUUID);
    user.setUsername(username);
    user.setPassword("tiger");
    user.setFirstName("Bruce");
    user.setLastName("Scott");
    user.setEmail("bruce1@scott.db");

    doNothing().when(teamService).createTeamForUser(any(UserEntity.class));
    when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(ZERO_RECORD);
    when(bCryptPasswordEncoder.encode(anyString())).thenReturn(cypheredPassword);
    when(userRepository.save(any(UserEntity.class))).thenReturn(null);

    UserEntity returnedUserEntity = classUnderTest.signUp(user);

    assertNotNull(returnedUserEntity);
    assertEquals(returnedUserEntity.getId(), userUUID);
    assertEquals(returnedUserEntity.getPassword(), cypheredPassword);
    assertEquals(returnedUserEntity.getUsername(), username);

  }

  @Test
  public void signUpGenericAlreadyExistsException() {
    final int ONE_RECORD = 1;

    UUID userUUID = UUID.randomUUID();
    User user = new User();
    user.setId(userUUID);
    user.setUsername("scott1");
    user.setPassword("tiger");
    user.setFirstName("Bruce");
    user.setLastName("Scott");
    user.setEmail("bruce1@scott.db");

    when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(ONE_RECORD);

    assertThrows(GenericAlreadyExistsException.class, () -> classUnderTest.signUp(user));

  }

  @Test
  public void signUser() {

    final boolean passwordsMatch = true;
    final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDEiLCJyb2xlcyI6WyJVU0VSIl0sImlzcyI6IkRyZWFtIFRlYW0gQVBJIiwiZXhwIjoxNjM5NDQ1NjA1LCJpYXQiOjE2Mzk0NDQ3MDV9.jFJV4vhRMllJf1SaR5An9GrSB10fX66GCYOJYbj0t3JDwSmN_b1DYrkseCJQgDbWj6pFF8gGV0gb19N99dAga-x6w3rK7bjL0zO7y03bhXYEWSOqCzSlw8FqiFGSTpcNqykU6hLFn8AuiAIGjT1Y9jyhPhbKlb7Lq7IhUcmJrMJsHkXXBlk5237NTtp_LZjK0Kl3gZm7vmkdYInliWbVsNr4ehc24vcfykMPMHgZugpSYyN62b4O58HWYHnBwuxYYWtkyRFyCl_z75K8GsOyuOZ80HqsjDHXMuK1v7LVlOgy5tJsnDypqJIBe1-hj-KWyvSyZnXpUQqTTGby2cRKcBGH0QYSWiy1pASGNjYPcqAHa2j4UFQwQFKSO6XNO6BKtQ0i6xiTgnF0tOKRK1Y4Orjegr6KmQvYom5ZX6rcTZniH86VSiQVTq4cAzKTzTsfguv_GGzwqfv3gDkwjhH1Vs1CDDXLLb5OXudnpu62o4PBPlUUKbSwE9ntj1aDWDdTsxl86Jsx3fMMOvkYHY9Bba8T82JNIlmFNQXF9sscBdUNyQx55UMLbEz7N72KI1DWgU2UU5Qh2KIhdkD7yL3CDL5-B7y7vBe3Etb1Sc4HZfAHNFjcFXK1elaIzZUFGaqswLswy8wRbYyU7Qa29pesGsKwmXNej8h6fpzoQZKy6hg";

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
    when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(passwordsMatch);
    when(userTokenRepository.deleteByUserId(any(UUID.class))).thenReturn(null);
    when(tokenManager.create(any(org.springframework.security.core.userdetails.User.class))).thenReturn(accessToken);
    when(userTokenRepository.save(any(UserTokenEntity.class))).thenReturn(null);

    SignedInUser returnedSignedInUser = classUnderTest.signUser(userEntity.getUsername(), userEntity.getPassword());

    assertNotNull(returnedSignedInUser);
    assertNotNull(returnedSignedInUser.getRefreshToken());
    assertEquals(returnedSignedInUser.getUserName(), userEntity.getUsername());
    assertEquals(returnedSignedInUser.getAccessToken(), accessToken);

  }

  @Test
  public void signUserUsernameNotFoundException() {

    String userName = null;
    String password = "tiger";

    assertThrows(UsernameNotFoundException.class, () -> classUnderTest.signUser(userName, password));
  }

  @Test
  public void signUserUserpasswordNotFoundException() {
    String userName = "scott1";
    String password = null;

    assertThrows(UsernameNotFoundException.class, () -> classUnderTest.signUser(userName, password));
  }

  @Test
  public void signUserInsufficientAuthenticationException() {
    String userName = "scott1";
    String password = "tiger";

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(InsufficientAuthenticationException.class, () -> classUnderTest.signUser(userName, password));

  }

  @Test
  public void getAccessToken() {

    String userName = "scott1";
    final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDEiLCJyb2xlcyI6WyJVU0VSIl0sImlzcyI6IkRyZWFtIFRlYW0gQVBJIiwiZXhwIjoxNjM5NDQ1NjA1LCJpYXQiOjE2Mzk0NDQ3MDV9.jFJV4vhRMllJf1SaR5An9GrSB10fX66GCYOJYbj0t3JDwSmN_b1DYrkseCJQgDbWj6pFF8gGV0gb19N99dAga-x6w3rK7bjL0zO7y03bhXYEWSOqCzSlw8FqiFGSTpcNqykU6hLFn8AuiAIGjT1Y9jyhPhbKlb7Lq7IhUcmJrMJsHkXXBlk5237NTtp_LZjK0Kl3gZm7vmkdYInliWbVsNr4ehc24vcfykMPMHgZugpSYyN62b4O58HWYHnBwuxYYWtkyRFyCl_z75K8GsOyuOZ80HqsjDHXMuK1v7LVlOgy5tJsnDypqJIBe1-hj-KWyvSyZnXpUQqTTGby2cRKcBGH0QYSWiy1pASGNjYPcqAHa2j4UFQwQFKSO6XNO6BKtQ0i6xiTgnF0tOKRK1Y4Orjegr6KmQvYom5ZX6rcTZniH86VSiQVTq4cAzKTzTsfguv_GGzwqfv3gDkwjhH1Vs1CDDXLLb5OXudnpu62o4PBPlUUKbSwE9ntj1aDWDdTsxl86Jsx3fMMOvkYHY9Bba8T82JNIlmFNQXF9sscBdUNyQx55UMLbEz7N72KI1DWgU2UU5Qh2KIhdkD7yL3CDL5-B7y7vBe3Etb1Sc4HZfAHNFjcFXK1elaIzZUFGaqswLswy8wRbYyU7Qa29pesGsKwmXNej8h6fpzoQZKy6hg";
    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    RefreshToken refreshTokenModel = new RefreshToken(refreshToken);

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername(userName);
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    UserTokenEntity userTokenEntity = new UserTokenEntity();
    userTokenEntity.setId(UUID.randomUUID());
    userTokenEntity.setUser(userEntity);
    userTokenEntity.setRefreshToken(refreshToken);

    when(userTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(userTokenEntity));
    when(tokenManager.create(any(org.springframework.security.core.userdetails.User.class))).thenReturn(accessToken);
    Optional<SignedInUser> returnedOptionalSignedInUser = classUnderTest.getAccessToken(refreshTokenModel);

    assertTrue(returnedOptionalSignedInUser.isPresent());
    assertEquals(returnedOptionalSignedInUser.get().getUserName(), userName);
    assertEquals(returnedOptionalSignedInUser.get().getAccessToken(), accessToken);
    assertEquals(returnedOptionalSignedInUser.get().getRefreshToken(), refreshToken);
  }

  @Test
  public void getAccessTokenInvalidRefreshTokenException() {

    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    RefreshToken refreshTokenModel = new RefreshToken(refreshToken);

    when(userTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.empty());

    assertThrows(InvalidRefreshTokenException.class, () -> classUnderTest.getAccessToken(refreshTokenModel));


  }

  @Test
  public void removeRefreshToken() {

    String userName = "scott1";
    final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDEiLCJyb2xlcyI6WyJVU0VSIl0sImlzcyI6IkRyZWFtIFRlYW0gQVBJIiwiZXhwIjoxNjM5NDQ1NjA1LCJpYXQiOjE2Mzk0NDQ3MDV9.jFJV4vhRMllJf1SaR5An9GrSB10fX66GCYOJYbj0t3JDwSmN_b1DYrkseCJQgDbWj6pFF8gGV0gb19N99dAga-x6w3rK7bjL0zO7y03bhXYEWSOqCzSlw8FqiFGSTpcNqykU6hLFn8AuiAIGjT1Y9jyhPhbKlb7Lq7IhUcmJrMJsHkXXBlk5237NTtp_LZjK0Kl3gZm7vmkdYInliWbVsNr4ehc24vcfykMPMHgZugpSYyN62b4O58HWYHnBwuxYYWtkyRFyCl_z75K8GsOyuOZ80HqsjDHXMuK1v7LVlOgy5tJsnDypqJIBe1-hj-KWyvSyZnXpUQqTTGby2cRKcBGH0QYSWiy1pASGNjYPcqAHa2j4UFQwQFKSO6XNO6BKtQ0i6xiTgnF0tOKRK1Y4Orjegr6KmQvYom5ZX6rcTZniH86VSiQVTq4cAzKTzTsfguv_GGzwqfv3gDkwjhH1Vs1CDDXLLb5OXudnpu62o4PBPlUUKbSwE9ntj1aDWDdTsxl86Jsx3fMMOvkYHY9Bba8T82JNIlmFNQXF9sscBdUNyQx55UMLbEz7N72KI1DWgU2UU5Qh2KIhdkD7yL3CDL5-B7y7vBe3Etb1Sc4HZfAHNFjcFXK1elaIzZUFGaqswLswy8wRbYyU7Qa29pesGsKwmXNej8h6fpzoQZKy6hg";
    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    RefreshToken refreshTokenModel = new RefreshToken(refreshToken);

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername(userName);
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    UserTokenEntity userTokenEntity = new UserTokenEntity();
    userTokenEntity.setId(UUID.randomUUID());
    userTokenEntity.setUser(userEntity);
    userTokenEntity.setRefreshToken(refreshToken);

    when(userTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(userTokenEntity));
    doNothing().when(userTokenRepository).delete(any(UserTokenEntity.class));

    classUnderTest.removeRefreshToken(refreshTokenModel);

    verify(userTokenRepository, times(1)).delete(any(UserTokenEntity.class));

  }

  @Test
  public void removeRefreshTokenInvalidRefreshTokenException() {
    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    RefreshToken refreshTokenModel = new RefreshToken(refreshToken);

    when(userTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.empty());
    assertThrows(InvalidRefreshTokenException.class, () -> classUnderTest.removeRefreshToken(refreshTokenModel));
  }
}