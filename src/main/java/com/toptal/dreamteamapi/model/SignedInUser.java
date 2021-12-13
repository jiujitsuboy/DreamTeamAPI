package com.toptal.dreamteamapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class SignedInUser extends RepresentationModel<SignedInUser> implements Serializable {

  @JsonProperty("userId")
  private String userId;
  @JsonProperty("refreshToken")
  private String refreshToken;
  @JsonProperty("accessToken")
  private String accessToken;
  @JsonProperty("username")
  private String userName;

}
