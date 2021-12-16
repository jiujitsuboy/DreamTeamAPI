package com.toptal.dreamteamapi.service;

import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.TransferList;
import java.util.List;

/**
 * Available operations for the Player's Transfer List
 */
public interface TransferListService {

  /**
   * Mark a player on sale by moving to the transfer list
   * @param playerId player's id
   * @return @Link {Player}
   */
  Player putOnSalePlayer(String playerId);

  /**
   * Buy a player available in the transfer list and make it part of the buying team.
   * @param playerId player's id
   * @param teamId destination team id of the purchase
   * @return @Link {Player}
   */
  Player buyPlayer(String playerId, String teamId);

  /**
   * Retrieve all the players and their values available in the transfer list
   * @return @Link{List} of @Link{TransferList}
   */
  List<TransferList> getTransferList();


}
