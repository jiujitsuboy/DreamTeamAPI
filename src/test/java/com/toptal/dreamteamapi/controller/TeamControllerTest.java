package com.toptal.dreamteamapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.dreamteamapi.TestConstants;
import com.toptal.dreamteamapi.configuration.AppConfig;
import com.toptal.dreamteamapi.entity.RoleEnum;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.hateoas.PlayerRepresentationModelAssembler;
import com.toptal.dreamteamapi.hateoas.TeamRepresentationModelAssembler;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.security.JwtManager;
import com.toptal.dreamteamapi.service.TeamService;
import com.toptal.dreamteamapi.service.impl.TeamServiceImpl;
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
@WebMvcTest(TeamController.class)
@Import(AppConfig.class)
@ComponentScan(basePackages = "com.toptal.dreamteamapi.security")
class TeamControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper json;
  @Autowired
  private JwtManager tokenManager;
  @MockBean
  private TeamService service;
  @SpyBean
  private TeamRepresentationModelAssembler teamAssembler;
  @SpyBean
  private PlayerRepresentationModelAssembler playerAssembler;

  private String token;

  @BeforeEach
  public void getToken(){
    token = TestConstants.getToken(tokenManager, RoleEnum.USER.name());
  }

  @Test
  public void updateTeam() throws Exception{
    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.OLD_TEAM_BUDGET, user);

    UserEntity userEntity = TestConstants.getTestUserEntity(user);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(team,userEntity);

    when(service.updateTeam(any(Team.class))).thenReturn(teamEntity);

    mvc.perform(patch("/api/v1/team")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(team))
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.name", is(team.getName())))
        .andExpect(jsonPath("$.country", is(team.getCountry())));
  }
  @Test
  public void getTeam() throws Exception{
    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.OLD_TEAM_BUDGET, user);

    UserEntity userEntity = TestConstants.getTestUserEntity(user);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(team,userEntity);

    when(service.getUserTeam(anyString())).thenReturn(teamEntity);

    mvc.perform(get("/api/v1/team/{userId}", userUUID.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(team))
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(team.getName())))
        .andExpect(jsonPath("$.country", is(team.getCountry())));
  }
  @Test
  public void getAllTeams() throws Exception{
    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    user.setRole(RoleEnum.ADMIN);
    Team team = TestConstants.getTestTeam(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.OLD_TEAM_BUDGET, user);

    UserEntity userEntity = TestConstants.getTestUserEntity(user);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(team,userEntity);

    token = TestConstants.getToken(tokenManager, user.getRole().name());

    when(service.getAllTeams()).thenReturn(List.of(teamEntity));

    mvc.perform(get("/api/v1/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(team))
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is(team.getName())))
        .andExpect(jsonPath("$[0].country", is(team.getCountry())));
  }
}