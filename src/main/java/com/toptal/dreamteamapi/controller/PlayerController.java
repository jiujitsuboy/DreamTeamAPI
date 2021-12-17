package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.toptal.dreamteamapi.entity.RoleEnum.Const;
import com.toptal.dreamteamapi.exception.NoSuchPlayerException;
import com.toptal.dreamteamapi.hateoas.PlayerRepresentationModelAssembler;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.service.PlayerService;
import com.toptal.dreamteamapi.service.impl.PlayerServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/player")
@Api(value = "Player Controller")
public class PlayerController {

  private final PlayerService service;
  private final PlayerRepresentationModelAssembler playerAssembler;

  public PlayerController(PlayerService service, PlayerRepresentationModelAssembler playerAssembler) {
    this.service = service;
    this.playerAssembler = playerAssembler;
  }

  @ApiOperation(value = "Update player", nickname = "updatePlayer", notes = "Allow to update firstname, lastname and country of the player-")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Update a couple of attributes of the player."),
      @ApiResponse(code = 500, message = "No Such Player Exception.") })
  @PatchMapping(value = "/")
  public ResponseEntity<Player> updatePlayer(@Valid @RequestBody(required = true) Player player) {
    return status(HttpStatus.ACCEPTED).body(playerAssembler.toModel(service.updatePlayer(player)));
  }

  @ApiOperation(value = "Get player", nickname = "getPlayer", notes = "Retrieve a single player identified by the specified ID-")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Retrieve the specified player."),
      @ApiResponse(code = 400, message = "No Such Player Exception.") })
  @GetMapping(value = "/{playerId}")
  public ResponseEntity<Player> getPlayer(@PathVariable("playerId") String playerId) {
      return ok(playerAssembler.toModel(service.getPlayerById(UUID.fromString(playerId))));
  }

  @PreAuthorize("hasRole('"+ Const.ADMIN+"')")
  @ApiOperation(value = "Get All players", nickname = "getAllPlayer", notes = "Retrieve all players-")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Retrieve all players.")})
  @GetMapping(value = "/")
  public ResponseEntity<List<Player>> getAllPlayer() {
      return ok(playerAssembler.toListModel(service.getAllPlayers()));
  }
}
