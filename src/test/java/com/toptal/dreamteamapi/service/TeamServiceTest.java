package com.toptal.dreamteamapi.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.toptal.dreamteamapi.TestConstants;
import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;

import com.toptal.dreamteamapi.exception.InsufficientTeamBudgedException;
import com.toptal.dreamteamapi.exception.NoSuchTeamException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.repository.PlayerRepository;
import com.toptal.dreamteamapi.repository.TeamRepository;
import com.toptal.dreamteamapi.service.impl.PlayerServiceImpl;
import com.toptal.dreamteamapi.service.impl.TeamServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

  @Mock
  private TeamRepository teamRepository;
  @Mock
  private PlayerRepository playerRepository;
  @Mock
  private PlayerServiceImpl playerService;
  @InjectMocks
  private TeamServiceImpl classUnderTest;

  @Test
  public void createTeamForUser() {

    UUID userUUID = UUID.randomUUID();
    UserEntity userEntity = TestConstants.getTestUserEntity(userUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);

    when(playerService.createPlayer(any(PlayerType.class), any(TeamEntity.class))).thenReturn(new PlayerEntity());

    classUnderTest.createTeamForUser(userEntity);

    verify(teamRepository, times(2)).save(any(TeamEntity.class));
  }

  @Test
  public void getUserTeam() {

    when(teamRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(new TeamEntity()));

    assertNotNull(classUnderTest.getUserTeam(UUID.randomUUID().toString()));
  }

  @Test
  public void getUserTeamNoSuchTeamException() {

    when(teamRepository.findByUserId(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(NoSuchTeamException.class, () -> classUnderTest.getUserTeam(UUID.randomUUID().toString()));
  }

  @Test
  public void getAllTeams() {

    Iterable<TeamEntity> expectedTeamEntities = List.of(new TeamEntity(), new TeamEntity());

    when(teamRepository.findAll()).thenReturn(expectedTeamEntities);

    assertNotNull(classUnderTest.getAllTeams());
  }

  @Test
  public void updateTeam() {

    UUID teamUUID = UUID.randomUUID();

    TeamEntity currentTeamEntity = new TeamEntity();
    currentTeamEntity.setId(teamUUID);
    currentTeamEntity.setName("TeamA");
    currentTeamEntity.setCountry("Colombia");

    Team updateTeamEntity = new Team();
    updateTeamEntity.setId(teamUUID);
    updateTeamEntity.setName("TeamB");
    updateTeamEntity.setCountry("China");

    when(teamRepository.findById(any(UUID.class))).thenReturn(Optional.of(currentTeamEntity));

    TeamEntity returnedTeamEntity = classUnderTest.updateTeam(updateTeamEntity);

    verify(teamRepository, times(1)).save(any(TeamEntity.class));
    assertNotNull(returnedTeamEntity);
    assertEquals(returnedTeamEntity.getName(), updateTeamEntity.getName());
    assertEquals(returnedTeamEntity.getCountry(), updateTeamEntity.getCountry());

  }

  @Test
  public void updateTeamNoSuchTeamException() {

    UUID teamUUID = UUID.randomUUID();

    Team updateTeamEntity = new Team();
    updateTeamEntity.setId(teamUUID);
    updateTeamEntity.setName("TeamB");
    updateTeamEntity.setCountry("China");

    when(teamRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
    assertThrows(NoSuchTeamException.class, () -> classUnderTest.updateTeam(updateTeamEntity));
  }

  @Test
  public void buyPlayer() {

    UUID playerUUID = UUID.randomUUID();
    UUID oldUserTeamUUID = UUID.randomUUID();
    UUID newUserTeamUUID = UUID.randomUUID();
    UUID oldTeamUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();

    Long newBudgetTeam = 10_000_000L;
    Long playerValue = 2_000_000L;

    UserEntity oldTeamUserEntity = TestConstants.getTestUserEntity(oldUserTeamUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    TeamEntity oldTeamEntity = TestConstants.getTestTeamEntity(oldTeamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,TestConstants.OLD_TEAM_BUDGET, oldTeamUserEntity);

    UserEntity newTeamUserEntity = TestConstants.getTestUserEntity(newUserTeamUUID,TestConstants.USER_NAME_B, TestConstants.USER_PASSWORD_B, TestConstants.USER_FIRST_NAME_B, TestConstants.USER_LAST_NAME_B, TestConstants.USER_EMAIL_B);
    TeamEntity newTeamEntity = TestConstants.getTestTeamEntity(newTeamUUID,TestConstants.NEW_TEAM_NAME,TestConstants.NEW_TEAM_VALUE,TestConstants.NEW_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,newBudgetTeam, newTeamUserEntity);

    PlayerEntity playerEntity = TestConstants.getTestPlayerEntity(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, playerValue, TestConstants.PLAYER_AGE, oldTeamEntity);

    oldTeamEntity.getPlayers().add(playerEntity);

    when(playerService.getPlayerById(playerUUID)).thenReturn(playerEntity);
    when(teamRepository.findById(oldTeamUUID)).thenReturn(Optional.of(oldTeamEntity));
    when(teamRepository.findById(newTeamUUID)).thenReturn(Optional.of(newTeamEntity));
    when(playerRepository.save(playerEntity)).thenReturn(playerEntity);
    when(teamRepository.save(oldTeamEntity)).thenReturn(oldTeamEntity);
    when(teamRepository.save(newTeamEntity)).thenReturn(newTeamEntity);

    Player returnedPlayer = classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString());

    assertNotNull(returnedPlayer);
    assertThat("Player assigned to buyer team", newTeamUUID, is(returnedPlayer.getTeam().getId()));
    assertThat("newTeam reduce budget by player's value", newTeamEntity.getBudget(), is(newBudgetTeam - playerValue));
    assertThat("oldTeam increase budget by player's value", oldTeamEntity.getBudget(), is(TestConstants.OLD_TEAM_BUDGET + playerValue));
    assertThat("Player value changed after the sell", playerEntity.getValue(), not(equalTo(playerValue)));
  }

  @Test
  public void buyPlayerNoSuchTeamException() {

    UUID playerUUID = UUID.randomUUID();
    UUID oldUserTeamUUID = UUID.randomUUID();
    UUID oldTeamUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    Long playerValue = 2_000_000L;

    UserEntity oldTeamUserEntity = TestConstants.getTestUserEntity(oldUserTeamUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    TeamEntity oldTeamEntity = TestConstants.getTestTeamEntity(oldTeamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,TestConstants.OLD_TEAM_BUDGET, oldTeamUserEntity);
    PlayerEntity playerEntity = TestConstants.getTestPlayerEntity(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, playerValue, TestConstants.PLAYER_AGE, oldTeamEntity);

    oldTeamEntity.getPlayers().add(playerEntity);

    when(playerService.getPlayerById(playerUUID)).thenReturn(playerEntity);
    when(teamRepository.findById(oldTeamUUID)).thenReturn(Optional.of(oldTeamEntity));
    when(teamRepository.findById(newTeamUUID)).thenReturn(Optional.empty());

    assertThrows(NoSuchTeamException.class, () -> classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString()));

  }

  @Test
  public void buyPlayerInsufficientTeamBudgedException() {

    UUID playerUUID = UUID.randomUUID();
    UUID oldUserTeamUUID = UUID.randomUUID();
    UUID oldTeamUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    String newTeamName = "TeamB";
    String newTeamCountry = "Colombia";
    String newUserTeamUserName = "bruce2";
    Long playerValue = 2_000_000L;
    Long newTeamBudget = 1_000_000L;
    Long newTeamValue = 20_000_000L;

    UserEntity oldTeamUserEntity = TestConstants.getTestUserEntity(oldUserTeamUUID,TestConstants.USER_NAME_A, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    TeamEntity oldTeamEntity = TestConstants.getTestTeamEntity(oldTeamUUID,TestConstants.OLD_TEAM_NAME,TestConstants.OLD_TEAM_VALUE,TestConstants.OLD_TEAM_COUNTRY,TestConstants.TEAM_PLAYERS_ENTITIES,TestConstants.OLD_TEAM_BUDGET, oldTeamUserEntity);

    UserEntity newTeamUserEntity = TestConstants.getTestUserEntity(oldUserTeamUUID,newUserTeamUserName, TestConstants.USER_PASSWORD_A, TestConstants.USER_FIRST_NAME_A, TestConstants.USER_LAST_NAME_A, TestConstants.USER_EMAIL_A);
    TeamEntity newTeamEntity = TestConstants.getTestTeamEntity(newTeamUUID,newTeamName,newTeamValue,newTeamCountry,TestConstants.TEAM_PLAYERS_ENTITIES,newTeamBudget, newTeamUserEntity);

    PlayerEntity playerEntity = TestConstants.getTestPlayerEntity(playerUUID,TestConstants.PLAYER_COUNTRY,TestConstants.PLAYER_FIRST_NAME, TestConstants.PLAYER_LAST_NAME, playerValue, TestConstants.PLAYER_AGE, oldTeamEntity);

    oldTeamEntity.getPlayers().add(playerEntity);

    when(playerService.getPlayerById(playerUUID)).thenReturn(playerEntity);
    when(teamRepository.findById(oldTeamUUID)).thenReturn(Optional.of(oldTeamEntity));
    when(teamRepository.findById(newTeamUUID)).thenReturn(Optional.of(newTeamEntity));

    assertThrows(InsufficientTeamBudgedException.class, () -> classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString()));

  }

}