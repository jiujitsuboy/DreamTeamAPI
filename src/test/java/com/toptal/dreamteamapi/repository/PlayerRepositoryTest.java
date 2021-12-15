package com.toptal.dreamteamapi.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PlayerRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private PlayerRepository playerRepository;

  @Test
  public void findByFirstnameAndLastname(){

    String firstName = "Lionel";
    String lastName = "Messi";
    String country = "Argentina";

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("scott1");
    userEntity.setPassword("tiger");
    userEntity.setFirstName("Bruce");
    userEntity.setLastName("Scott");
    userEntity.setEmail("bruce1@scott.db");

    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setName("TeamB");
    teamEntity.setValue(20_000_000L);
    teamEntity.setCountry("Colombia");
    teamEntity.setBudget(5_000_000L);
    teamEntity.setUser(userEntity);

    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setFirstname(firstName);
    playerEntity.setLastname(lastName);
    playerEntity.setValue(1_000_000);
    playerEntity.setCountry(country);
    playerEntity.setType(PlayerType.DEFENDER);
    playerEntity.setTeam(teamEntity);

    entityManager.persist(userEntity);
    entityManager.persist(teamEntity);
    entityManager.persist(playerEntity);
    entityManager.flush();

    Optional<PlayerEntity> optPlayerEntity =  playerRepository.findByFirstnameAndLastname(firstName,lastName);

    assertTrue(optPlayerEntity.isPresent());
    assertEquals(optPlayerEntity.get().getFirstname(),firstName);
    assertEquals(optPlayerEntity.get().getLastname(),lastName);
  }

}