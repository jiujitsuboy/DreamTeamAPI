package com.toptal.dreamteamapi.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.service.TeamService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

  private final TeamService service;

  public TeamController(TeamService service) {
    this.service = service;
  }

  @PutMapping(
      value = "/",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<Team> updateTeam(@Valid @RequestBody(required = false) Team team) {
    service.updateTeam(team);
    return status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(
      value = "/{userId}",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<Team> getTeam(@PathVariable("userId") String userId) {

    return ok(service.getUserTeam(userId));
  }

  @GetMapping(
      value = "/",
      produces = {"application/json"},
      consumes = {"application/json"}
  )
  public ResponseEntity<List<Team>> getAllTeams() {

    return ok(service.getAllTeams());
  }
}
