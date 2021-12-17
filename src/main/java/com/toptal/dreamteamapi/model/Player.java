package com.toptal.dreamteamapi.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
public class Player extends RepresentationModel<Player> {
  private UUID id;
  private String firstname;
  private String lastname;
  private String country;
  private PlayerType type;
  private byte age;
  private long value;
  private Team team;

}
