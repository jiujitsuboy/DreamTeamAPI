package com.toptal.dreamteamapi.service;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import java.util.UUID;

/**
 * Player available operations
 */
public interface PlayerService {

  /**
   * Create a player of an specific type and assigned to a team
   * @param playerType type of the player
   * @param teamEntity team owner of the player
   * @return @link{PlayerEntity}
   */
  PlayerEntity createPlayer(PlayerType playerType, TeamEntity teamEntity);

  /**
   * Allow update the names and country of the player
   * @param player Exisiting player to be updated
   * @return @link{PlayerEntity}
   */
  PlayerEntity updatePlayer(Player player);

  /**
   * Return the player identified by the Id
   * @param playerId player's id
   * @return @link{PlayerEntity}
   */
  PlayerEntity getPlayerById(UUID playerId);

  /**
   * Return all the players in the system.
   * @return @link{PlayerEntity}
   */
  Iterable<PlayerEntity> getAllPlayers();
}
