package com.toptal.dreamteamapi.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Team {

  private String name;
  private String country;
  private long value;
  private long budget;
  private Set<Player> players;
}
