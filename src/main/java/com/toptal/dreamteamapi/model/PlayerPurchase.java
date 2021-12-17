package com.toptal.dreamteamapi.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PlayerPurchase {
  private String playerid;
  private String teamid;
}
