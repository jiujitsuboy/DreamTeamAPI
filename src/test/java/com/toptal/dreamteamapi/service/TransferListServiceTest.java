package com.toptal.dreamteamapi.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.TransferListEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import com.toptal.dreamteamapi.model.Team;
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

    UUID playerUUID = UUID.randomUUID();

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setId(UUID.randomUUID());
    teamEntity.setName("TeamB");
    teamEntity.setValue(20_000_000L);
    teamEntity.setCountry("Colombia");
    teamEntity.setBudget(5_000_000L);
    teamEntity.setUser(userEntity);

    PlayerEntity expectedPlayerEntity = new PlayerEntity();
    expectedPlayerEntity.setId(playerUUID);
    expectedPlayerEntity.setFirstname("PlayerFirstname");
    expectedPlayerEntity.setType(PlayerType.DEFENDER);
    expectedPlayerEntity.setValue(1_000_000L);
    expectedPlayerEntity.setTeam(teamEntity);

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
    UUID playerUUID = UUID.randomUUID();
    UUID oldTeamUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    Long playerValue = 2_000_000L;
    Long oldTeamBudget = 5_000_000L;
    Long newTeamBudget = 1_000_000L;
    Long oldTeamValue = 20_000_000L;
    Long newTeamValue = 20_000_000L;

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    TeamEntity newTeamEntity = new TeamEntity();
    newTeamEntity.setId(newTeamUUID);
    newTeamEntity.setName("TeamB");
    newTeamEntity.setValue(newTeamValue);
    newTeamEntity.setCountry("Colombia");
    newTeamEntity.setBudget(newTeamBudget);
    newTeamEntity.setUser(userEntity);

    TeamEntity oldTeamEntity = new TeamEntity();
    oldTeamEntity.setId(oldTeamUUID);
    oldTeamEntity.setBudget(oldTeamBudget);
    oldTeamEntity.setValue(oldTeamValue);

    PlayerEntity expectedPlayerEntity = new PlayerEntity();
    expectedPlayerEntity.setId(playerUUID);
    expectedPlayerEntity.setFirstname("PlayerFirstname");
    expectedPlayerEntity.setType(PlayerType.DEFENDER);
    expectedPlayerEntity.setValue(playerValue);
    expectedPlayerEntity.setTeam(newTeamEntity);

    Team team = new Team();
    team.setId(newTeamUUID);
    team.setName(newTeamEntity.getName());

    Player expectedPlayer = new Player();
    expectedPlayer.setId(playerUUID);
    expectedPlayer.setFirstname("PlayerFirstname");
    expectedPlayer.setType(PlayerType.DEFENDER);
    expectedPlayer.setValue(playerValue);
    expectedPlayer.setTeam(team);

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
    UUID playerUUID = UUID.randomUUID();
    UUID newTeamUUID = UUID.randomUUID();
    Long playerValue = 2_000_000L;
    Long teamBudget = 1_000_000L;
    Long teamValue = 20_000_000L;

    UserEntity userEntity = new UserEntity();
    userEntity.setId(UUID.randomUUID());
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setId(newTeamUUID);
    teamEntity.setName("TeamB");
    teamEntity.setValue(teamValue);
    teamEntity.setCountry("Colombia");
    teamEntity.setBudget(teamBudget);
    teamEntity.setUser(userEntity);

    PlayerEntity expectedPlayerEntity = new PlayerEntity();
    expectedPlayerEntity.setId(playerUUID);
    expectedPlayerEntity.setFirstname("PlayerFirstname");
    expectedPlayerEntity.setType(PlayerType.DEFENDER);
    expectedPlayerEntity.setValue(playerValue);
    expectedPlayerEntity.setTeam(teamEntity);

    TransferListEntity transferListEntity = new TransferListEntity();
    transferListEntity.setId(transferListUUID);
    transferListEntity.setPlayer(expectedPlayerEntity);
    transferListEntity.setValue(expectedPlayerEntity.getValue());

    Iterable<TransferListEntity> expectedTransferList = List.of(transferListEntity);
    when(transferListRepository.findAll()).thenReturn(expectedTransferList);

    assertNotNull(classUnderTest.getTransferList());
  }

}