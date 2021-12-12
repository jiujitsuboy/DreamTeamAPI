package com.toptal.dreamteamapi.entity;


import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class UserEntity {

  @Id
  @GeneratedValue
  @Column(name="ID", updatable = false, nullable = false)
  private UUID id;

  @NotNull(message = "User name is required.")
  @Basic(optional = false)
  @Column(name = "USERNAME")
  private String username;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "ROLE")
  @Enumerated(EnumType.STRING)
  private RoleEnum role = RoleEnum.USER;
}
