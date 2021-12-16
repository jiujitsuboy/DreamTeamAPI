package com.toptal.dreamteamapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.toptal.dreamteamapi.TestConstants;
import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.repository.PlayerRepository;
import com.toptal.dreamteamapi.service.impl.PlayerServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

  @Mock
  private PlayerRepository playerRepository;

  @InjectMocks
  private PlayerServiceImpl classUnderTest;

  @Test
  public void createPlayer(){

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();

    UserEntity userEntity = TestConstants.getTestUserEntity(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,TestConstants.OLD_TEAM_BUDGET, userEntity);

    PlayerEntity playerEntity = classUnderTest.createPlayer(PlayerType.ATTACKER, teamEntity);
    assertNotNull(playerEntity);
    assertEquals(PlayerType.ATTACKER,playerEntity.getType());
  }

  @Test
  public void updatePlayer(){

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    String expectedFirstname = "Yerri";
    String expectedLastname = "Mina";
    String expectedCountry = "Colombia";

    User user = TestConstants.getTestUser(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    Team team = TestConstants.getTestTeam(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.OLD_TEAM_BUDGET, user);
    Player previousPlayer = TestConstants.getTestPlayer(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);
    Player updatedPlayer = TestConstants.getTestPlayer(playerUUID,expectedCountry,expectedFirstname, expectedLastname, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, team);

    UserEntity userEntity = TestConstants.getTestUserEntity(user);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(team,userEntity);
    PlayerEntity previousPlayerEntity = TestConstants.getTestPlayerEntity(previousPlayer,teamEntity);

    when(playerRepository.findById(any(UUID.class))).thenReturn(Optional.of(previousPlayerEntity));

    PlayerEntity returnedPlayerEntity = classUnderTest.updatePlayer(updatedPlayer);

    assertNotNull(returnedPlayerEntity);
    assertEquals(returnedPlayerEntity.getFirstname(),updatedPlayer.getFirstname());
    assertEquals(returnedPlayerEntity.getLastname(),updatedPlayer.getLastname());
    assertEquals(returnedPlayerEntity.getCountry(),updatedPlayer.getCountry());

  }
  @Test
  public void getPlayerByIdNoSuchPlayerException(){
    when(playerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
    assertThrows(NoSuchPlayerException.class,()->classUnderTest.getPlayerById(UUID.randomUUID()));
  }

  @Test
  public void getAllPlayers(){

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    UserEntity userEntity = TestConstants.getTestUserEntity(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,TestConstants.OLD_TEAM_BUDGET, userEntity);
    PlayerEntity playerEntity = TestConstants.getTestPlayerEntity(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, teamEntity);

    Iterable<PlayerEntity> expectedPlayerEntities = List.of(playerEntity, playerEntity);
    when(playerRepository.findAll()).thenReturn(expectedPlayerEntities);
    assertNotNull(classUnderTest.getAllPlayers());
  }
}