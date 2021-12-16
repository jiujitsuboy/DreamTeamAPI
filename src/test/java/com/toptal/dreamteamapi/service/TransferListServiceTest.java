package com.toptal.dreamteamapi.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.toptal.dreamteamapi.TestConstants;
import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.TransferListEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.repository.TransferListRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferListServiceTest {

  @Mock
  private TransferListRepository transferListRepository;
  @Mock
  private TeamService teamService;
  @Mock
  private PlayerService playerService;
  @InjectMocks
  private TransferListService classUnderTest;

  @Test
  public void putOnSalePlayer() {

    UUID userUUID = UUID.randomUUID();
    UUID teamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    UserEntity userEntity = TestConstants.getTestUserEntity(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    TeamEntity teamEntity = TestConstants.getTestTeamEntity(teamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,TestConstants.OLD_TEAM_BUDGET, userEntity);
    PlayerEntity expectedPlayerEntity = TestConstants.getTestPlayerEntity(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, teamEntity);

    when(playerService.getPlayerById(any(UUID.class))).thenReturn(expectedPlayerEntity);

    Player returnedPlayer = classUnderTest.putOnSalePlayer(playerUUID.toString());

    assertNotNull(returnedPlayer);
    assertEquals(returnedPlayer.getFirstname(), expectedPlayerEntity.getFirstname());
    assertEquals(returnedPlayer.getType(), expectedPlayerEntity.getType());
    assertEquals(returnedPlayer.getValue(), expectedPlayerEntity.getValue());

  }

  @Test
  public void buyPlayer() {

    UUID transferListUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    User newUser = TestConstants.getTestUser(newTeamUUID,TestConstants.USER_NAME_B, TestConstants.USER_PASSWORD_B, TestConstants.USER_FIRST_NAME_B, TestConstants.USER_LAST_NAME_B, TestConstants.USER_EMAIL_B);
    Team newTeam = TestConstants.getTestTeam(newTeamUUID,TestConstants.NEW_TEAM_NAME,TestConstants.NEW_TEAM_VALUE,TestConstants.NEW_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS,TestConstants.NEW_TEAM_BUDGET, newUser);
    Player expectedPlayer = TestConstants.getTestPlayer(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, newTeam);

    UserEntity oldUserEntity = TestConstants.getTestUserEntity(newUser);
    TeamEntity oldTeamEntity = TestConstants.getTestTeamEntity(newTeam, oldUserEntity);
    PlayerEntity expectedPlayerEntity = TestConstants.getTestPlayerEntity(expectedPlayer, oldTeamEntity);

    TransferListEntity transferListEntity = new TransferListEntity();
    transferListEntity.setId(transferListUUID);
    transferListEntity.setPlayer(expectedPlayerEntity);
    transferListEntity.setValue(expectedPlayerEntity.getValue());

    when(transferListRepository.findByPlayerId(any(UUID.class))).thenReturn(Optional.of(transferListEntity));
    when(teamService.buyPlayer(playerUUID.toString(), newTeamUUID.toString())).thenReturn(expectedPlayer);
    doNothing().when(transferListRepository).delete(transferListEntity);

    Player returnedPlayer = classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString());

    assertNotNull(returnedPlayer);
    assertThat("Player assigned to buyer team", newTeamUUID, is(returnedPlayer.getTeam().getId()));
    assertEquals(expectedPlayer.getId(), returnedPlayer.getId());
    assertEquals(expectedPlayer.getFirstname(), returnedPlayer.getFirstname());
    assertEquals(expectedPlayer.getType(), returnedPlayer.getType());
    assertEquals(expectedPlayer.getValue(), returnedPlayer.getValue());
  }

  @Test
  public void buyPlayerNoSuchPlayerException() {

    UUID playerUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();

    when(transferListRepository.findByPlayerId(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(NoSuchPlayerException.class, () -> classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString()));
  }

  @Test
  public void getTransferList() {

    UUID transferListUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    UUID playerUUID = UUID.randomUUID();

    UserEntity newUserEntity = TestConstants.getTestUserEntity(newTeamUUID,TestConstants.USER_NAME_B, TestConstants.USER_PASSWORD_B, TestConstants.USER_FIRST_NAME_B, TestConstants.USER_LAST_NAME_B, TestConstants.USER_EMAIL_B);
    TeamEntity newTeamEntity = TestConstants.getTestTeamEntity(newTeamUUID,TestConstants.NEW_TEAM_NAME,TestConstants.NEW_TEAM_VALUE,TestConstants.NEW_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,TestConstants.NEW_TEAM_BUDGET, newUserEntity);
    PlayerEntity expectedPlayerEntity = TestConstants.getTestPlayerEntity(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, TestConstants.PLAYER_VALUE, TestConstants.PLAYER_AGE, newTeamEntity);

    TransferListEntity transferListEntity = new TransferListEntity();
    transferListEntity.setId(transferListUUID);
    transferListEntity.setPlayer(expectedPlayerEntity);
    transferListEntity.setValue(expectedPlayerEntity.getValue());

    Iterable<TransferListEntity> expectedTransferList = List.of(transferListEntity);
    when(transferListRepository.findAll()).thenReturn(expectedTransferList);

    assertNotNull(classUnderTest.getTransferList());
  }

}