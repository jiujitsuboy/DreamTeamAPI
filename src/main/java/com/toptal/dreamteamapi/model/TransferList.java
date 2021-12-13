package com.toptal.dreamteamapi.model;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import lombok.Data;

@Data
public class TransferList {
  private UUID id;
  private Player player;
  private long value;

}
