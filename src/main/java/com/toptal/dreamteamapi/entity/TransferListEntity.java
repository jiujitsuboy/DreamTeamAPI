package com.toptal.dreamteamapi.entity;

import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "transfer_list")
@Data
public class TransferListEntity {

  @Id
  @GeneratedValue
  @Column(name="ID", updatable = false, nullable = false)
  private UUID id;

  @OneToOne
  private PlayerEntity player;

  @NotNull(message = "Value is required.")
  @Basic(optional = false)
  @Column(name = "VALUE")
  private String value;
}
