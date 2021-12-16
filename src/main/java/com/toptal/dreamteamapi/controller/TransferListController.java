package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.http.ResponseEntity.ok;

import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerPurchase;
import com.toptal.dreamteamapi.model.TransferList;
import com.toptal.dreamteamapi.service.TransferListService;
import com.toptal.dreamteamapi.service.impl.TransferListServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "Transfer List Controller")
public class TransferListController {

  private final TransferListService service;

  public TransferListController(TransferListService service) {
    this.service = service;
  }

  @ApiOperation(value = "Get Transfer List", nickname = "getTransferList", notes = "Retrieve the list with all the available player for transfer-")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Retrieve transfer list.")})
  @GetMapping(value = "/transfer-list")
  public ResponseEntity<List<TransferList>> getTransferList() {
    return ok(service.getTransferList());
  }


  @ApiOperation(value = "Transfer Player", nickname = "transferPlayer", notes = "Transfer a player to the Transfer list-")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Transfer a player to the  list."),
      @ApiResponse(code = 500, message = "No Such Player Exception")})
  @PostMapping(value = "/transfer-list/{playerId}")
  public ResponseEntity<Player> transferPlayer(@PathVariable("playerId") String playerId) {
    return status(HttpStatus.ACCEPTED).body(service.putOnSalePlayer(playerId));
  }

  @ApiOperation(value = "Buy Player", nickname = "buyPlayer", notes = "Buy a player offered in the Transfer list-")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "buy player from the  list."),
      @ApiResponse(code = 500, message = "No Such Player Exception")})
  @DeleteMapping(value = "/transfer-list")
  public ResponseEntity<Player> buyPlayer(@Valid @RequestBody(required = false) PlayerPurchase playerPurchase) {
    return ok(service.buyPlayer(playerPurchase.getPlayerid(), playerPurchase.getTeamid()));
  }
}
