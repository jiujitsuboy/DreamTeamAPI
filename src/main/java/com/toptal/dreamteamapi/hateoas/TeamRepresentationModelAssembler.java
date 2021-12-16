package com.toptal.dreamteamapi.hateoas;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.toptal.dreamteamapi.controller.PlayerController;
import com.toptal.dreamteamapi.controller.TeamController;
import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.service.Util;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TeamRepresentationModelAssembler extends RepresentationModelAssemblerSupport<TeamEntity, Team> {

  private PlayerRepresentationModelAssembler playerAssembler;

  public TeamRepresentationModelAssembler(PlayerRepresentationModelAssembler playerAssembler) {
    super(PlayerController.class, Team.class);
    this.playerAssembler = playerAssembler;
  }

  @Override
  public Team toModel(TeamEntity entity) {
    Team team = (Team) Util.toModel(entity);
    team.getUser().setPassword("Ciphered...");
    team.setPlayers(team.getPlayers().stream().map(player -> {
      player.setTeam(null);
      player.add(linkTo(methodOn(PlayerController.class).getPlayer(player.getId().toString())).withSelfRel());
      return player;
    }).collect(Collectors.toList()));

    return team;
  }

  public List<Team> toListModel(Iterable<TeamEntity> entities) {
    if (Objects.isNull(entities)) {
      return Collections.emptyList();
    }
    return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e))
        .collect(toList());
  }

}
