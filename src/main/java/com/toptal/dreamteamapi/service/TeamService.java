package com.toptal.dreamteamapi.service;

import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.Team;

/**
 * Team available operations
 */
public interface TeamService {

  /**
   * Create the only team a user can have. Randomly create 20 players
   * @param userEntity User owner of this team
   */
  void createTeamForUser(UserEntity userEntity);

  /**
   * Return the team identified by the user Id
   * @param userId User id owner of this team
   * @return
   */
  TeamEntity getUserTeam(String userId);

  /**
   * Allow update the name and country of the team
   * @param team
   * @return
   */
  TeamEntity updateTeam(Team team);

  /**
   * Return all the Teams in the system.
   * @return
   */
  Iterable<TeamEntity> getAllTeams();

  /**
   * Buy a player from the transfer list and make it part of the team
   * @param playerId player id to buy
   * @param teamId destination team id of the purchase
   * @return @Link{Player}
   */
  Player buyPlayer(String playerId, String teamId);


}
