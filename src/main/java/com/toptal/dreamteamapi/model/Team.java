package com.toptal.dreamteamapi.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

  private UUID id;
  private String name;
  private String country;
  private long value;
  private long budget;
  private List<Player> players;
  private User user;
}
