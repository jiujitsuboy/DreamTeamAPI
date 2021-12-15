package com.toptal.dreamteamapi.entity;


import com.toptal.dreamteamapi.model.PlayerType;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "player")
@Data
public class PlayerEntity {

  @Id
  @GeneratedValue
  @Column(name="ID", updatable = false, nullable = false)
  private UUID id;

  @NotNull(message = "Player firstname is required.")
  @Basic(optional = false)
  @Column(name = "FIRST_NAME")
  private String firstname;

  @NotNull(message = "Player lastname is required.")
  @Basic(optional = false)
  @Column(name = "LAST_NAME")
  private String lastname;

  @Column(name = "TYPE")
  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private PlayerType type;

  @NotNull(message = "Country is required.")
  @Basic(optional = false)
  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "AGE")
  private byte age;

  @Column(name = "VALUE")
  private long value;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "team_id")
  private TeamEntity team;
}
