package com.toptal.dreamteamapi.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
  private UUID id;
  private String firstname;
  private String lastname;
  private String country;
  private byte age;
  private long value;
  private Team team;

}
