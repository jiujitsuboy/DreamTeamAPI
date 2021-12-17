package com.toptal.dreamteamapi.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
public class Team  extends RepresentationModel<Team> {

  private UUID id;
  private String name;
  private String country;
  private long value;
  private long budget;
  private List<Player> players;
  private User user;
}
