package com.toptal.dreamteamapi.service;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.repository.PlayerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

  private final long PLAYER_VALUE = 1_000_000;

  private PlayerRepository playerRepository;

  public PlayerService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Transactional
  public PlayerEntity createPlayer(PlayerType playerType, TeamEntity teamEntity) {
    String[] name = Util.NAMES.get((int) Math.round(Math.random() * (Util.NAMES.size() - 1))).split(" ");
    String country = Util.COUNTRIES.get((int) Math.round(Math.random() * (Util.COUNTRIES.size() - 1)));
    byte age = (byte) (18 + Math.random() * 22);

    PlayerEntity playerEntity = new PlayerEntity();

    playerEntity.setFirstname(name[0]);
    playerEntity.setLastname(name[1]);
    playerEntity.setCountry(country);
    playerEntity.setAge(age);
    playerEntity.setValue(PLAYER_VALUE);
    playerEntity.setTeam(teamEntity);
    playerEntity.setType(playerType);

    playerRepository.save(playerEntity);

    return playerEntity;
  }

  @Transactional
  public PlayerEntity updatePlayer(String playerId, Player player) {
    PlayerEntity playerEntity = getPlayerById(UUID.fromString(playerId));

    playerEntity.setFirstname(player.getFirstname());
    playerEntity.setLastname(player.getLastname());
    playerEntity.setCountry(player.getCountry());

    playerRepository.save(playerEntity);

    return playerEntity;
  }

  public PlayerEntity getPlayerById(UUID playerId) {
    return playerRepository.findById(playerId)
        .orElseThrow(() -> new NoSuchPlayerException(String.format("Player with id %s doesn't exists", playerId)));
  }

  public Iterable<PlayerEntity> getAllPlayers() {
    List<Player> players = new ArrayList<>();
    Iterable<PlayerEntity> playerEntities =  playerRepository.findAll();
    playerEntities.forEach(player->players.add((Player) Util.toModel(player)));
    return playerEntities;
  }
}
