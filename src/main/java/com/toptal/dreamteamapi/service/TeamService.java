package com.toptal.dreamteamapi.service;

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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

  private final long PLAYER_VALUE = 1_000_000;
  private final long INITIAL_TEAM_BUDGET = 5_000_000;

  private TeamRepository teamRepository;
  private PlayerRepository playerRepository;
  private PlayerService playerService;

  public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository, PlayerService playerService) {
    this.teamRepository = teamRepository;
    this.playerService = playerService;
    this.playerRepository = playerRepository;
  }

  @Transactional
  public void createTeamForUser(UserEntity userEntity) {

    List<PlayerEntity> playersEntity = new ArrayList<>();
    String teamName = userEntity.getEmail().substring(0, userEntity.getEmail().indexOf("@"));
    String country = Util.COUNTRIES.get((int) Math.round(Math.random() * (Util.COUNTRIES.size() - 1)));

    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setName(teamName);
    teamEntity.setCountry(country);
    teamEntity.setValue(PLAYER_VALUE * 20);
    teamEntity.setBudget(INITIAL_TEAM_BUDGET);
    teamEntity.setUser(userEntity);

    teamRepository.save(teamEntity);

    IntStream.rangeClosed(1, 3).forEach(index -> {
      playersEntity.add(playerService.createPlayer(PlayerType.GOALKEEPER, teamEntity));
    });

    IntStream.rangeClosed(1, 6).forEach(index -> {
      playersEntity.add(playerService.createPlayer(PlayerType.DEFENDER, teamEntity));
    });

    IntStream.rangeClosed(1, 6).forEach(index -> {
      playersEntity.add(playerService.createPlayer(PlayerType.MIDFIELDER, teamEntity));
    });

    IntStream.rangeClosed(1, 5).forEach(index -> {
      playersEntity.add(playerService.createPlayer(PlayerType.ATTACKER, teamEntity));
    });

    teamEntity.setPlayers(playersEntity);

    teamRepository.save(teamEntity);
  }

  public TeamEntity getUserTeam(String userId) {
    return teamRepository.findByUserId(UUID.fromString(userId))
        .orElseThrow(() -> new NoSuchTeamException(String.format("User with id %s doesn't not have team", userId)));
  }

  public Iterable<TeamEntity> getAllTeams() {
    return teamRepository.findAll();
  }

  @Transactional
  public TeamEntity updateTeam(Team team) {
    TeamEntity teamEntity = teamRepository.findById(team.getId())
        .orElseThrow(() -> new NoSuchTeamException(String.format("Team with id %s doesn't not exits", team.getId())));

    teamEntity.setName(team.getName());

    teamEntity.setCountry(team.getCountry());

    teamRepository.save(teamEntity);

    return teamEntity;
  }

  @Transactional
  public Player buyPlayer(String playerId, String teamId) {
    PlayerEntity playerEntity = playerService.getPlayerById(UUID.fromString(playerId));
    TeamEntity oldTeamEntity = teamRepository.findById(playerEntity.getTeam().getId()).get();
    TeamEntity newTeamEntity = teamRepository.findById(UUID.fromString(teamId))
        .orElseThrow(() -> new NoSuchTeamException(String.format("Team with id %s doesn't not exists", teamId)));

    if (newTeamEntity.getBudget() < playerEntity.getValue()) {
      throw new InsufficientTeamBudgedException(
          String.format("Team %s doesn't have enough budget %s to pay %s for player %s", teamId, newTeamEntity.getBudget(),
              playerEntity.getValue(), playerId));
    }
    newTeamEntity.setBudget(newTeamEntity.getBudget() - playerEntity.getValue());
    oldTeamEntity.setBudget(oldTeamEntity.getBudget() + playerEntity.getValue());

    oldTeamEntity.setPlayers(
        oldTeamEntity.getPlayers().stream().filter(player -> player.getId().equals(playerId)).collect(Collectors.toList()));
    teamRepository.save(oldTeamEntity);

    playerEntity.setTeam(newTeamEntity);
    playerEntity.setValue((long) (playerEntity.getValue() + (playerEntity.getValue() * (10 + Math.random() * 90) / 100)));
    playerRepository.save(playerEntity);

    newTeamEntity.setValue(newTeamEntity.getValue() + playerEntity.getValue());
    oldTeamEntity.setValue(oldTeamEntity.getValue() + playerEntity.getValue());
    newTeamEntity.getPlayers().add(playerEntity);
    teamRepository.save(newTeamEntity);

    return (Player) Util.toModel(playerEntity);
  }
}
