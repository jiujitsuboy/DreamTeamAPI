package com.toptal.dreamteamapi.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

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
import java.util.ArrayList;
import java.util.Arrays;
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
  private PlayerService playerService;
  @InjectMocks
  private TeamService classUnderTest;

  @Test
  public void createTeamForUser() {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    when(playerService.createPlayer(any(PlayerType.class), any(TeamEntity.class))).thenReturn(new PlayerEntity());

    classUnderTest.createTeamForUser(userEntity);
    verify(teamRepository, times(2)).save(any(TeamEntity.class));
  }

  @Test
  public void getUserTeam() {

    TeamEntity expectedTeamEntity = new TeamEntity();

    when(teamRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(expectedTeamEntity));

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
    TeamEntity currentTeamEntity = new TeamEntity();
    currentTeamEntity.setName("TeamA");
    Team updateTeamEntity = new Team();
    updateTeamEntity.setId(UUID.randomUUID());
    updateTeamEntity.setName("TeamB");
    updateTeamEntity.setCountry("China");

    when(teamRepository.findById(any(UUID.class))).thenReturn(Optional.of(currentTeamEntity));
    classUnderTest.updateTeam(updateTeamEntity);
    verify(teamRepository, times(1)).save(any(TeamEntity.class));
  }

  @Test
  public void updateTeamNoSuchTeamException() {
    TeamEntity currentTeamEntity = new TeamEntity();
    currentTeamEntity.setName("TeamA");
    Team updateTeamEntity = new Team();
    updateTeamEntity.setId(UUID.randomUUID());
    updateTeamEntity.setName("TeamB");
    updateTeamEntity.setCountry("China");

    when(teamRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
    assertThrows(NoSuchTeamException.class, () -> classUnderTest.updateTeam(updateTeamEntity));
  }

  @Test
  public void buyPlayer() {
    UUID playerUUID = UUID.randomUUID();
    UUID oldTeamUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    Long playerValue = 2_000_000L;
    Long oldTeamBudget = 5_000_000L;
    Long newTeamBudget = 10_000_000L;
    Long oldTeamValue = 20_000_000L;
    Long newTeamValue = 20_000_000L;

    TeamEntity oldTeamEntity = new TeamEntity();
    oldTeamEntity.setId(oldTeamUUID);
    oldTeamEntity.setBudget(oldTeamBudget);
    oldTeamEntity.setValue(oldTeamValue);

    TeamEntity newTeamEntity = new TeamEntity();
    newTeamEntity.setId(newTeamUUID);
    newTeamEntity.setName("TeamB");
    newTeamEntity.setValue(newTeamValue);
    newTeamEntity.setCountry("Colombia");
    newTeamEntity.setBudget(newTeamBudget);

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(playerUUID);
    playerEntity.setValue(playerValue);
    playerEntity.setTeam(oldTeamEntity);

    oldTeamEntity.setPlayers(Arrays.asList(playerEntity));
    newTeamEntity.setPlayers(new ArrayList<>());
    newTeamEntity.setUser(userEntity);

    when(playerService.getPlayerById(playerUUID)).thenReturn(playerEntity);
    when(teamRepository.findById(oldTeamUUID)).thenReturn(Optional.of(oldTeamEntity));
    when(teamRepository.findById(newTeamUUID)).thenReturn(Optional.of(newTeamEntity));
    when(playerRepository.save(playerEntity)).thenReturn(playerEntity);
    when(teamRepository.save(oldTeamEntity)).thenReturn(oldTeamEntity);
    when(teamRepository.save(newTeamEntity)).thenReturn(newTeamEntity);

    Player returnedPlayer = classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString());

    assertNotNull(returnedPlayer);
    assertThat("Player assigned to buyer team", newTeamUUID, is(returnedPlayer.getTeam().getId()));
    assertThat("newTeam reduce budget by player's value", newTeamEntity.getBudget(), is(newTeamBudget - playerValue));
    assertThat("oldTeam increase budget by player's value", oldTeamEntity.getBudget(), is(oldTeamBudget + playerValue));
    assertThat("Player value changed after the sell", playerEntity.getValue(), not(equalTo(playerValue)));
  }

  @Test
  public void buyPlayerNoSuchTeamException() {
    UUID playerUUID = UUID.randomUUID();
    UUID oldTeamUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    Long playerValue = 2_000_000L;
    Long oldTeamBudget = 5_000_000L;
    Long newTeamBudget = 10_000_000L;
    Long oldTeamValue = 20_000_000L;
    Long newTeamValue = 20_000_000L;

    TeamEntity oldTeamEntity = new TeamEntity();
    oldTeamEntity.setId(oldTeamUUID);
    oldTeamEntity.setBudget(oldTeamBudget);
    oldTeamEntity.setValue(oldTeamValue);

    TeamEntity newTeamEntity = new TeamEntity();
    newTeamEntity.setId(newTeamUUID);
    newTeamEntity.setName("TeamB");
    newTeamEntity.setValue(newTeamValue);
    newTeamEntity.setCountry("Colombia");
    newTeamEntity.setBudget(newTeamBudget);

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(playerUUID);
    playerEntity.setValue(playerValue);
    playerEntity.setTeam(oldTeamEntity);

    oldTeamEntity.setPlayers(Arrays.asList(playerEntity));
    newTeamEntity.setPlayers(new ArrayList<>());
    newTeamEntity.setUser(userEntity);

    when(playerService.getPlayerById(playerUUID)).thenReturn(playerEntity);
    when(teamRepository.findById(oldTeamUUID)).thenReturn(Optional.of(oldTeamEntity));
    when(teamRepository.findById(newTeamUUID)).thenReturn(Optional.empty());

    assertThrows(NoSuchTeamException.class, () -> classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString()));

  }

  @Test
  public void buyPlayerInsufficientTeamBudgedException() {
    UUID playerUUID = UUID.randomUUID();
    UUID oldTeamUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    Long playerValue = 2_000_000L;
    Long oldTeamBudget = 5_000_000L;
    Long newTeamBudget = 1_000_000L;
    Long oldTeamValue = 20_000_000L;
    Long newTeamValue = 20_000_000L;

    TeamEntity oldTeamEntity = new TeamEntity();
    oldTeamEntity.setId(oldTeamUUID);
    oldTeamEntity.setBudget(oldTeamBudget);
    oldTeamEntity.setValue(oldTeamValue);

    TeamEntity newTeamEntity = new TeamEntity();
    newTeamEntity.setId(newTeamUUID);
    newTeamEntity.setName("TeamB");
    newTeamEntity.setValue(newTeamValue);
    newTeamEntity.setCountry("Colombia");
    newTeamEntity.setBudget(newTeamBudget);

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(playerUUID);
    playerEntity.setValue(playerValue);
    playerEntity.setTeam(oldTeamEntity);

    oldTeamEntity.setPlayers(Arrays.asList(playerEntity));
    newTeamEntity.setPlayers(new ArrayList<>());
    newTeamEntity.setUser(userEntity);

    when(playerService.getPlayerById(playerUUID)).thenReturn(playerEntity);
    when(teamRepository.findById(oldTeamUUID)).thenReturn(Optional.of(oldTeamEntity));
    when(teamRepository.findById(newTeamUUID)).thenReturn(Optional.of(newTeamEntity));

    assertThrows(InsufficientTeamBudgedException.class, () -> classUnderTest.buyPlayer(playerUUID.toString(), newTeamUUID.toString()));

  }

}