package com.toptal.dreamteamapi.service.impl;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.repository.PlayerRepository;
import com.toptal.dreamteamapi.service.PlayerService;
import com.toptal.dreamteamapi.service.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerServiceImpl implements PlayerService {

  private final long PLAYER_VALUE = 1_000_000;

  private PlayerRepository playerRepository;

  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
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

  @Override
  @Transactional
  public PlayerEntity updatePlayer(Player player) {
    PlayerEntity playerEntity = getPlayerById(player.getId());

    playerEntity.setFirstname(player.getFirstname());
    playerEntity.setLastname(player.getLastname());
    playerEntity.setCountry(player.getCountry());

    playerRepository.save(playerEntity);

    return playerEntity;
  }

  @Override
  public PlayerEntity getPlayerById(UUID playerId) {
    return playerRepository.findById(playerId)
        .orElseThrow(() -> new NoSuchPlayerException(String.format("Player with id %s doesn't exists", playerId)));
  }

  @Override
  public Iterable<PlayerEntity> getAllPlayers() {
    List<Player> players = new ArrayList<>();
    Iterable<PlayerEntity> playerEntities =  playerRepository.findAll();
    playerEntities.forEach(player->players.add((Player) Util.toModel(player)));
    return playerEntities;
  }
}
