package com.toptal.dreamteamapi.model;

public enum PlayerType {

  GOALKEEPER("goalkeeper"),
  DEFENDER("defender"),
  MIDFIELDER("midfielder"),
  ATTACKER("attacker");

  private String type;

  PlayerType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public String toString() {
    return String.valueOf(type);
  }
}
