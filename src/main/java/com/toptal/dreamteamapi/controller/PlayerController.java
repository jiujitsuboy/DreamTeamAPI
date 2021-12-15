package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.hateoas.PlayerRepresentationModelAssembler;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.service.PlayerService;
import com.toptal.dreamteamapi.service.Util;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {

  private final PlayerService service;
  private final PlayerRepresentationModelAssembler playerAssembler;

  public PlayerController(PlayerService service, PlayerRepresentationModelAssembler playerAssembler) {
    this.service = service;
    this.playerAssembler = playerAssembler;
  }

  @PutMapping(
      value = "/{playerId}",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<Player> updatePlayer(@PathVariable("playerId") String playerId, @Valid @RequestBody(required = true) Player player) {
    return status(HttpStatus.ACCEPTED).body(playerAssembler.toModel(service.updatePlayer(playerId,player)));
  }

  @GetMapping(
      value = "/{playerId}",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<Player> getPlayer(@PathVariable("playerId") String playerId) {

    try{;
      return ok(playerAssembler.toModel(service.getPlayerById(UUID.fromString(playerId))));
    }
    catch (NoSuchPlayerException ex){
      return status(HttpStatus.BAD_REQUEST).build();
    }

  }

  @GetMapping(
      value = "/",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<List<Player>> getAllPlayer() {
      return ok(playerAssembler.toListModel(service.getAllPlayers()));
  }
}
