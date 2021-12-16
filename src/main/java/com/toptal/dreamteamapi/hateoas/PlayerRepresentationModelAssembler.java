package com.toptal.dreamteamapi.hateoas;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.toptal.dreamteamapi.controller.TeamController;
import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.service.Util;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class PlayerRepresentationModelAssembler extends RepresentationModelAssemblerSupport<PlayerEntity, Player> {

  public PlayerRepresentationModelAssembler() {
    super(TeamController.class, Player.class);
  }

  @Override
  public Player toModel(PlayerEntity entity) {
    Player player= (Player)Util.toModel(entity);
    player.getTeam().getUser().setPassword("Ciphered...");
    player.add(linkTo(methodOn(TeamController.class).getTeam(player.getTeam().getUser().getId().toString())).withRel("user-team"));
    return player;
  }

  public List<Player> toListModel(Iterable<PlayerEntity> entities) {
    if (Objects.isNull(entities)) {
      return Collections.emptyList();
    }
    return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e))
        .collect(toList());
  }

}
