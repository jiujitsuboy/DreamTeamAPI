package com.toptal.dreamteamapi.service;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TransferListEntity;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.TransferList;
import com.toptal.dreamteamapi.repository.TransferListRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferListService {

  private TransferListRepository transferListRepository;
  private TeamService teamService;
  private PlayerService playerService;

  public TransferListService(TransferListRepository transferListRepository, TeamService teamService, PlayerService playerService) {
    this.transferListRepository = transferListRepository;
    this.teamService = teamService;
    this.playerService = playerService;
  }

  @Transactional
  public Player putOnSalePlayer(String playerId) {
    PlayerEntity playerEntity = playerService.getPlayerById(UUID.fromString(playerId));
    TransferListEntity transferListEntity = new TransferListEntity();
    transferListEntity.setPlayer(playerEntity);
    transferListEntity.setValue(playerEntity.getValue());
    transferListRepository.save(transferListEntity);
    return (Player) Util.toModel(playerEntity);
  }

  @Transactional
  public Player buyPlayer(String playerId, String teamId) {
    TransferListEntity transferListEntity = transferListRepository.findByPlayerId(UUID.fromString(playerId))
        .orElseThrow(() -> new NoSuchPlayerException(String.format("Player with id %s doesn't exists in Transfer List", playerId)));
    Player player = teamService.buyPlayer(playerId, teamId);
    transferListRepository.delete(transferListEntity);
    return player;
  }

  public List<TransferList> getTransferList(){
    List<TransferList> transferLists = new ArrayList<>();
    Iterable<TransferListEntity> transferListEntities = transferListRepository.findAll();
    transferListEntities.forEach(transfer->transferLists.add((TransferList) Util.toModel(transfer)));
    return transferLists;
  }

}
