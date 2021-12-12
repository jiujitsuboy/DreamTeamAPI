package com.toptal.dreamteamapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class SignInReq extends RepresentationModel<SignInReq>  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("username")
  private String username;

  @JsonProperty("password")
  private String password;
}

