package com.toptal.dreamteamapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.dreamteamapi.TestConstants;
import com.toptal.dreamteamapi.configuration.AppConfig;
import com.toptal.dreamteamapi.entity.RoleEnum;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerPurchase;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.model.TransferList;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.security.JwtManager;
import com.toptal.dreamteamapi.service.TransferListService;
import com.toptal.dreamteamapi.service.impl.TransferListServiceImpl;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransferListController.class)
@Import(AppConfig.class)
@ComponentScan(basePackages = "com.toptal.dreamteamapi.security")
class TransferListControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper json;
  @Autowired
  private JwtManager tokenManager;
  @MockBean
  private TransferListService service;

  private String token;

  @BeforeEach
  public void getToken() {
    token = TestConstants.getToken(tokenManager, RoleEnum.USER.name());
  }

  @Test
  public void getTransferList() throws Exception {

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();
    UUID transferListUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID, TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A,
        TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID, TestConstants.OLD_TEAM_NAME, TestConstants.OLD_TEAM_VALUE, TestConstants.OLD_TEAM_COUNTRY,
        TestConstants.TEAM_PLAYERS, TestConstants.OLD_TEAM_BUDGET, user);
    Player player = TestConstants.getTestPlayer(playerUUID, TestConstants.PLAYER_COUNTRY, TestConstants.PLAYER_FIRST_NAME,
        TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);
    TransferList transferList = TestConstants.getTransferList(transferListUUID, player);

    when(service.getTransferList()).thenReturn(List.of(transferList));

    mvc.perform(get("/api/v1/player/transfer-list")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(transferListUUID.toString())))
        .andExpect(jsonPath("$[0].player.id", is(playerUUID.toString())))
        .andExpect(jsonPath("$[0].value", is((int) player.getValue())));

  }

  @Test
  public void transferPlayer() throws Exception {

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    User user = TestConstants.getTestUser(userUUID, TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A,
        TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID, TestConstants.OLD_TEAM_NAME, TestConstants.OLD_TEAM_VALUE, TestConstants.OLD_TEAM_COUNTRY,
        TestConstants.TEAM_PLAYERS, TestConstants.OLD_TEAM_BUDGET, user);
    Player player = TestConstants.getTestPlayer(playerUUID, TestConstants.PLAYER_COUNTRY, TestConstants.PLAYER_FIRST_NAME,
        TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);

    when(service.putOnSalePlayer(anyString())).thenReturn(player);

    mvc.perform(post("/api/v1/player/transfer-list/{playerId}", playerUUID.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id", is(playerUUID.toString())));

  }

  @Test
  public void buyPlayer() throws Exception {

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    PlayerPurchase playerPurchase = new PlayerPurchase();
    playerPurchase.setPlayerid(playerUUID.toString());
    playerPurchase.setTeamid(teamUUID.toString());

    User user = TestConstants.getTestUser(userUUID, TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A,
        TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID, TestConstants.OLD_TEAM_NAME, TestConstants.OLD_TEAM_VALUE, TestConstants.OLD_TEAM_COUNTRY,
        TestConstants.TEAM_PLAYERS, TestConstants.OLD_TEAM_BUDGET, user);
    Player player = TestConstants.getTestPlayer(playerUUID, TestConstants.PLAYER_COUNTRY, TestConstants.PLAYER_FIRST_NAME,
        TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);

    when(service.buyPlayer(anyString(), anyString())).thenReturn(player);

    mvc.perform(delete("/api/v1/player/transfer-list/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(playerPurchase))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(playerUUID.toString())))
        .andExpect(jsonPath("$.team.id", is(teamUUID.toString())));

  }
}