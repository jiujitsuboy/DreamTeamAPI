package com.toptal.dreamteamapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import com.toptal.dreamteamapi.repository.PlayerRepository;
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
  private PlayerService classUnderTest;

  @Test
  public void createPlayer(){

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

    PlayerEntity playerEntity = classUnderTest.createPlayer(PlayerType.ATTACKER, teamEntity);
    assertNotNull(playerEntity);
    assertEquals(PlayerType.ATTACKER,playerEntity.getType());
  }

  @Test
  public void updatePlayer(){
    UUID playerUUID = UUID.randomUUID();
    String previousFirstname = "playerFirstName";
    String previousLastname = "playerLastName";
    String previousCountry = "Colombia";
    String expectedFirstname = "Lionel";
    String expectedLastname = "Messi";
    String expectedCountry = "Argentina";


    PlayerEntity previousPlayerEntity = new PlayerEntity();
    previousPlayerEntity.setId(playerUUID);
    previousPlayerEntity.setFirstname(previousFirstname);
    previousPlayerEntity.setLastname(previousLastname);
    previousPlayerEntity.setCountry(previousCountry);

    Player updatedPlayer = new Player();
    updatedPlayer.setId(playerUUID);
    updatedPlayer.setFirstname(expectedFirstname);
    updatedPlayer.setLastname(expectedLastname);
    updatedPlayer.setCountry(expectedCountry);

    when(playerRepository.findById(any(UUID.class))).thenReturn(Optional.of(previousPlayerEntity));

    PlayerEntity returnedPlayerEntity = classUnderTest.updatePlayer(playerUUID.toString(),updatedPlayer);

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

    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(UUID.randomUUID());
    playerEntity.setValue(1_000_000);
    playerEntity.setTeam(teamEntity);

    Iterable<PlayerEntity> expectedPlayerEntities = List.of(playerEntity, playerEntity);
    when(playerRepository.findAll()).thenReturn(expectedPlayerEntities);
    assertNotNull(classUnderTest.getAllPlayers());
  }


}