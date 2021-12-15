package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.http.ResponseEntity.ok;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerPurchase;
import com.toptal.dreamteamapi.model.TransferList;
import com.toptal.dreamteamapi.service.TransferListService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/player")
public class TransferListController {

  private final TransferListService service;

  public TransferListController(TransferListService service) {
    this.service = service;
  }

  @GetMapping(
      value = "/transfer-list",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<List<TransferList>> getTransferList() {
    return ok(service.getTransferList());
  }

  @PostMapping(
      value = "/transfer-list/{playerId}",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<Player> transferPlayer(@PathVariable("playerId") String playerId) {
    return status(HttpStatus.ACCEPTED).body(service.putOnSalePlayer(playerId));
  }

  @DeleteMapping(
      value = "/transfer-list",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<Player> buyPlayer(@Valid @RequestBody(required = false) PlayerPurchase playerPurchase) {
    return ok(service.buyPlayer(playerPurchase.getPlayerid(), playerPurchase.getTeamid()));
  }
}
