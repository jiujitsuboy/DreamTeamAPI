package com.toptal.dreamteamapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.dreamteamapi.TestConstants;
import com.toptal.dreamteamapi.configuration.AppConfig;
import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.RoleEnum;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.hateoas.PlayerRepresentationModelAssembler;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.security.JwtManager;
import com.toptal.dreamteamapi.service.PlayerService;
import com.toptal.dreamteamapi.service.impl.PlayerServiceImpl;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PlayerController.class)
@Import(AppConfig.class)
@ComponentScan(basePackages = "com.toptal.dreamteamapi.security")
class PlayerControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper json;
  @Autowired
  private JwtManager tokenManager;
  @MockBean
  private PlayerService service;
  @SpyBean
  private PlayerRepresentationModelAssembler playerAssembler;

  private String token;

  @BeforeEach
  public void getToken(){
    token = TestConstants.getToken(tokenManager, RoleEnum.USER.name());
  }

  @Test
  public void updatePlayer() throws Exception{

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.OLD_TEAM_BUDGET, user);
    Player player = TestConstants.getTestPlayer(playerUUID,TestConstants.PLAYER_COUNTRY, TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);

    UserEntity userEntity = TestConstants.getTestUserEntity(user);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(team,userEntity);
    PlayerEntity playerEntity = TestConstants.getTestPlayerEntity(player,teamEntity);

    when(service.updatePlayer(any(Player.class))).thenReturn(playerEntity);

    mvc.perform(patch("/api/v1/player/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(player))
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.firstname", is(player.getFirstname())))
        .andExpect(jsonPath("$.lastname", is(player.getLastname())))
        .andExpect(jsonPath("$.country", is(player.getCountry())));
  }

  @Test
  public void getPlayer() throws Exception{

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.OLD_TEAM_BUDGET, user);
    Player player = TestConstants.getTestPlayer(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);

    UserEntity userEntity = TestConstants.getTestUserEntity(user);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(team,userEntity);
    PlayerEntity playerEntity = TestConstants.getTestPlayerEntity(player,teamEntity);

    when(service.getPlayerById(any(UUID.class))).thenReturn(playerEntity);

    mvc.perform(get("/api/v1/player/{playerId}",playerUUID.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstname", is(player.getFirstname())))
        .andExpect(jsonPath("$.lastname", is(player.getLastname())))
        .andExpect(jsonPath("$.country", is(player.getCountry())));
  }

  @Test
  public void getPlayerNoSuchPlayerException() throws Exception{

    UUID playerUUID = UUID.randomUUID();

    when(service.getPlayerById(any(UUID.class))).thenThrow(NoSuchPlayerException.class);

    mvc.perform(get("/api/v1/player/{playerId}",playerUUID.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void getAllPlayer() throws Exception{

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    user.setRole(RoleEnum.ADMIN);
    Team team = TestConstants.getTestTeam(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.OLD_TEAM_BUDGET, user);
    Player player = TestConstants.getTestPlayer(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);

    UserEntity userEntity = TestConstants.getTestUserEntity(user);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(team,userEntity);
    PlayerEntity playerEntity = TestConstants.getTestPlayerEntity(player,teamEntity);

    token = TestConstants.getToken(tokenManager, user.getRole().name());

    when(service.getAllPlayers()).thenReturn(List.of(playerEntity));

    mvc.perform(get("/api/v1/player/")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].firstname", is(player.getFirstname())))
        .andExpect(jsonPath("$[0].lastname", is(player.getLastname())))
        .andExpect(jsonPath("$[0].country", is(player.getCountry())));
  }
}