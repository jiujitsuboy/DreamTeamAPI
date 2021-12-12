package com.toptal.dreamteamapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RefreshToken extends RepresentationModel<RefreshToken>  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("refreshToken")
  private String refreshToken;

  public RefreshToken refreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }
}

