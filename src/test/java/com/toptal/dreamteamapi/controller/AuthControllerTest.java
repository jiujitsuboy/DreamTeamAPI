package com.toptal.dreamteamapi.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.exception.InvalidRefreshTokenException;
import com.toptal.dreamteamapi.hateoas.UserRepresentationModelAssembler;
import com.toptal.dreamteamapi.model.RefreshToken;
import com.toptal.dreamteamapi.model.SignInReq;
import com.toptal.dreamteamapi.model.SignedInUser;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.service.UserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper json;
  @MockBean
  private UserService userService;
  @SpyBean
  UserRepresentationModelAssembler userAssembler;

  @Test
  public void getAccessToken() throws Exception {
    String userName = "scott1";
    final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDEiLCJyb2xlcyI6WyJVU0VSIl0sImlzcyI6IkRyZWFtIFRlYW0gQVBJIiwiZXhwIjoxNjM5NDQ1NjA1LCJpYXQiOjE2Mzk0NDQ3MDV9.jFJV4vhRMllJf1SaR5An9GrSB10fX66GCYOJYbj0t3JDwSmN_b1DYrkseCJQgDbWj6pFF8gGV0gb19N99dAga-x6w3rK7bjL0zO7y03bhXYEWSOqCzSlw8FqiFGSTpcNqykU6hLFn8AuiAIGjT1Y9jyhPhbKlb7Lq7IhUcmJrMJsHkXXBlk5237NTtp_LZjK0Kl3gZm7vmkdYInliWbVsNr4ehc24vcfykMPMHgZugpSYyN62b4O58HWYHnBwuxYYWtkyRFyCl_z75K8GsOyuOZ80HqsjDHXMuK1v7LVlOgy5tJsnDypqJIBe1-hj-KWyvSyZnXpUQqTTGby2cRKcBGH0QYSWiy1pASGNjYPcqAHa2j4UFQwQFKSO6XNO6BKtQ0i6xiTgnF0tOKRK1Y4Orjegr6KmQvYom5ZX6rcTZniH86VSiQVTq4cAzKTzTsfguv_GGzwqfv3gDkwjhH1Vs1CDDXLLb5OXudnpu62o4PBPlUUKbSwE9ntj1aDWDdTsxl86Jsx3fMMOvkYHY9Bba8T82JNIlmFNQXF9sscBdUNyQx55UMLbEz7N72KI1DWgU2UU5Qh2KIhdkD7yL3CDL5-B7y7vBe3Etb1Sc4HZfAHNFjcFXK1elaIzZUFGaqswLswy8wRbYyU7Qa29pesGsKwmXNej8h6fpzoQZKy6hg";
    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    RefreshToken refreshTokenModel = new RefreshToken(refreshToken);
    SignedInUser signedInUser = new SignedInUser();
    signedInUser.setUserName(userName);
    signedInUser.setAccessToken(accessToken);
    signedInUser.setRefreshToken(refreshToken);

    when(userService.getAccessToken(any(RefreshToken.class))).thenReturn(Optional.of(signedInUser));

    mvc.perform(post("/api/v1/auth/token/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(refreshTokenModel)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is(userName)))
        .andExpect(jsonPath("$.accessToken", is(accessToken)))
        .andExpect(jsonPath("$.refreshToken", is(refreshToken)));
  }

  @Test
  public void signIn() throws Exception {
    String userName = "scott1";
    String password = "tiger";

    final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDEiLCJyb2xlcyI6WyJVU0VSIl0sImlzcyI6IkRyZWFtIFRlYW0gQVBJIiwiZXhwIjoxNjM5NDQ1NjA1LCJpYXQiOjE2Mzk0NDQ3MDV9.jFJV4vhRMllJf1SaR5An9GrSB10fX66GCYOJYbj0t3JDwSmN_b1DYrkseCJQgDbWj6pFF8gGV0gb19N99dAga-x6w3rK7bjL0zO7y03bhXYEWSOqCzSlw8FqiFGSTpcNqykU6hLFn8AuiAIGjT1Y9jyhPhbKlb7Lq7IhUcmJrMJsHkXXBlk5237NTtp_LZjK0Kl3gZm7vmkdYInliWbVsNr4ehc24vcfykMPMHgZugpSYyN62b4O58HWYHnBwuxYYWtkyRFyCl_z75K8GsOyuOZ80HqsjDHXMuK1v7LVlOgy5tJsnDypqJIBe1-hj-KWyvSyZnXpUQqTTGby2cRKcBGH0QYSWiy1pASGNjYPcqAHa2j4UFQwQFKSO6XNO6BKtQ0i6xiTgnF0tOKRK1Y4Orjegr6KmQvYom5ZX6rcTZniH86VSiQVTq4cAzKTzTsfguv_GGzwqfv3gDkwjhH1Vs1CDDXLLb5OXudnpu62o4PBPlUUKbSwE9ntj1aDWDdTsxl86Jsx3fMMOvkYHY9Bba8T82JNIlmFNQXF9sscBdUNyQx55UMLbEz7N72KI1DWgU2UU5Qh2KIhdkD7yL3CDL5-B7y7vBe3Etb1Sc4HZfAHNFjcFXK1elaIzZUFGaqswLswy8wRbYyU7Qa29pesGsKwmXNej8h6fpzoQZKy6hg";
    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    SignedInUser signedInUser = new SignedInUser();
    signedInUser.setUserName(userName);
    signedInUser.setAccessToken(accessToken);
    signedInUser.setRefreshToken(refreshToken);

    SignInReq signInReq = new SignInReq();
    signInReq.setUserName(userName);
    signInReq.setPassword(password);

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername(userName);
    userEntity.setPassword(password);
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    when(userService.signUser(anyString(), anyString())).thenReturn(signedInUser);
    when(userService.findUserByUserName(anyString())).thenReturn(Optional.of(userEntity));


    mvc.perform(post("/api/v1/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(signInReq)))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.username", is(userName)))
        .andExpect(jsonPath("$.accessToken", is(accessToken)))
        .andExpect(jsonPath("$.refreshToken", is(refreshToken)));
  }

  @Test
  public void signInUsernameNotFoundException() throws Exception {
    String userName = "scott1";
    String password = "tiger";

    SignInReq signInReq = new SignInReq();
    signInReq.setUserName(userName);
    signInReq.setPassword(password);

    when(userService.signUser(anyString(), anyString())).thenThrow(UsernameNotFoundException.class);


    mvc.perform(post("/api/v1/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(signInReq)))
        .andExpect(status().isBadRequest())
        //.andExpect(jsonPath("$", is("Invalid user.")))
    ;
  }

  @Test
  public void signInInsufficientAuthenticationException() throws Exception {
    String userName = "scott1";
    String password = "tiger";

    SignInReq signInReq = new SignInReq();
    signInReq.setUserName(userName);
    signInReq.setPassword(password);

    when(userService.signUser(anyString(), anyString())).thenThrow(InsufficientAuthenticationException.class);


    mvc.perform(post("/api/v1/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(signInReq)))
        .andExpect(status().isUnauthorized())
    //.andExpect(jsonPath("$", is("Invalid user.")))
    ;
  }
  @Test
  public void signOut() throws Exception {

    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    RefreshToken refreshTokenModel = new RefreshToken(refreshToken);

    doNothing().when(userService).removeRefreshToken(any(RefreshToken.class));


    mvc.perform(delete("/api/v1/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(refreshTokenModel)))
        .andExpect(status().isAccepted());
  }

  @Test
  public void signOutInvalidRefreshTokenException() throws Exception {

    final String refreshToken = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";

    RefreshToken refreshTokenModel = new RefreshToken(refreshToken);

    doThrow(InvalidRefreshTokenException.class).when(userService).removeRefreshToken(any(RefreshToken.class));

    mvc.perform(delete("/api/v1/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(refreshTokenModel)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void signUp() throws Exception {

    UUID userUUID = UUID.randomUUID();
    User user = new User();
    user.setId(userUUID);
    user.setUsername("scott1");
    user.setPassword("tiger");
    user.setFirstName("Bruce");
    user.setLastName("Scott");
    user.setEmail("bruce1@scott.db");

    UserEntity userEntity = new UserEntity();
    userEntity.setId(user.getId());
    userEntity.setUsername(user.getUsername());
    userEntity.setPassword(user.getPassword());
    userEntity.setFirstName(user.getFirstName());
    userEntity.setLastName(user.getLastName());
    userEntity.setEmail(user.getEmail());

    when(userService.signUp(any(User.class))).thenReturn(userEntity);

    mvc.perform(post("/api/v1/auth/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(user)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id",is(userUUID.toString())))
        .andExpect(jsonPath("$.username",is(user.getUsername())))
        .andExpect(jsonPath("$.password",is("Ciphered...")))
        .andExpect(jsonPath("$.firstName",is(user.getFirstName())))
        .andExpect(jsonPath("$.lastName",is(user.getLastName())))
        .andExpect(jsonPath("$.email",is(user.getEmail())));
  }
}