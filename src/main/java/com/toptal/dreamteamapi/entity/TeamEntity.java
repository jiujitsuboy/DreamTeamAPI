package com.toptal.dreamteamapi.entity;


import java.util.Set;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "team")
@Data
public class TeamEntity {

  @Id
  @GeneratedValue
  @Column(name="ID", updatable = false, nullable = false)
  private UUID id;

  @NotNull(message = "Team name is required.")
  @Basic(optional = false)
  @Column(name = "NAME")
  private String name;

  @NotNull(message = "Country is required.")
  @Basic(optional = false)
  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "VALUE")
  private Long value;

  @Column(name = "BUDGET")
  private Long budget;

  @OneToOne
  private UserEntity user;

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PlayerEntity> players;
}
