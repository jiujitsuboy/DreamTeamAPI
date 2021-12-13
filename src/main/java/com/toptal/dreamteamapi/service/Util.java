package com.toptal.dreamteamapi.service;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.TransferListEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.SignedInUser;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.model.TransferList;
import com.toptal.dreamteamapi.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

public class Util {

  public static final List<String> COUNTRIES = List.of("Colombia", "Argentina", "Brazil", "Chile");
  public static final List<String> NAMES = List.of("Patrick Mahomes",
      "Tom Brady",
      "Ezekiel Elliott",
      "Saquon Barkley",
      "Aaron Rodgers",
      "Drew Brees",
      "Todd Gurley",
      "Khalil Mack");

  public static <M> Object toEntity(M model) {
    Object entity = null;
    if (model instanceof Team) {
      entity = new TeamEntity();
    } else if (model instanceof Player) {
      entity = new PlayerEntity();
    } else if (model instanceof User) {
      entity = new UserEntity();
    }
    BeanUtils.copyProperties(model, entity);
    return entity;
  }

  public static <E> Object toModel(E entity) {
    Object model = null;
    if (entity instanceof TeamEntity) {
      model = new Team();
      ((Team) model).setUser((User) toModel(((TeamEntity) entity).getUser()));
      ((Team) model).setPlayers(
          ((TeamEntity) entity).getPlayers().stream().map(player -> (Player) toModel(player)).collect(Collectors.toList()));
    } else if (entity instanceof PlayerEntity) {
      model = new Player();
      TeamEntity teamEntity = ((PlayerEntity) entity).getTeam();
      Team team = new Team();
      BeanUtils.copyProperties(teamEntity, team);
      ((Player) model).setTeam(team);
    } else if (entity instanceof UserEntity) {
      model = new User();
    } else if (entity instanceof TransferListEntity) {
      model = new TransferList();
      ((TransferList) model).setPlayer((Player) toModel(((TransferListEntity) entity).getPlayer()));
    }
    BeanUtils.copyProperties(entity, model);
    return model;
  }

  public static <E, M> Object updateEntity(E entity, M model) {
    BeanUtils.copyProperties(model, entity);
    return entity;
  }
}
