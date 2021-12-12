package com.toptal.dreamteamapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {

  private String firstname;
  private String lastname;
  private String country;
  private byte age;
  private double value;
  private Team team;

}
