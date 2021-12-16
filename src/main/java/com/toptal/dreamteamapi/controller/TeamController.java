package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.toptal.dreamteamapi.hateoas.TeamRepresentationModelAssembler;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.service.TeamService;
import com.toptal.dreamteamapi.service.impl.TeamServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Api(value = "Team Controller")
public class TeamController {

  private final TeamService service;
  private final TeamRepresentationModelAssembler teamAssembler;

  public TeamController(TeamService service, TeamRepresentationModelAssembler teamAssembler) {
    this.teamAssembler = teamAssembler;
    this.service = service;
  }

  @ApiOperation(value = "Update Team", nickname = "updateTeam", notes = "Allow to update name and country of the team-")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Update a couple of attributes of the team."),
      @ApiResponse(code = 500, message = "No Such Team Exception.") })
  @PatchMapping(value = "/team")
  public ResponseEntity<Team> updateTeam(@Valid @RequestBody(required = false) Team team) {
    return status(HttpStatus.ACCEPTED).body(teamAssembler.toModel(service.updateTeam(team)));
  }

  @ApiOperation(value = "Get Team", nickname = "getTeam", notes = "Retrieve a single team identified by the specified user ID-")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Retrieve the specified team."),
      @ApiResponse(code = 500, message = "No Such Team Exception.") })
  @GetMapping(value = "/team/{userId}")
  public ResponseEntity<Team> getTeam(@PathVariable("userId") String userId) {

    return ok(teamAssembler.toModel(service.getUserTeam(userId)));
  }

  @ApiOperation(value = "Get All Teams", nickname = "getAllTeams", notes = "Retrieve all the teams-")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Retrieve all the team.")})
  @GetMapping(value = "/teams")
  public ResponseEntity<List<Team>> getAllTeams() {

    return ok(teamAssembler.toListModel(service.getAllTeams()));
  }
}
