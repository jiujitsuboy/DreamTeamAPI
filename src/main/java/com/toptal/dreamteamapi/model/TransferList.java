package com.toptal.dreamteamapi.model;

import java.util.UUID;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TransferList extends RepresentationModel<TransferList> {
  private UUID id;
  private Player player;
  private long value;

}
