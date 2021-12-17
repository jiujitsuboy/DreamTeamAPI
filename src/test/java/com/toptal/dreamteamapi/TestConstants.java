package com.toptal.dreamteamapi;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.RoleEnum;
import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.model.Player;
import com.toptal.dreamteamapi.model.PlayerType;
import com.toptal.dreamteamapi.model.Team;
import com.toptal.dreamteamapi.model.TransferList;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.security.JwtManager;
import com.toptal.dreamteamapi.service.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestConstants {

  public static final String PLAYER_FIRST_NAME = "Lionel";
  public static final String PLAYER_LAST_NAME = "Messi";
  public static final String PLAYER_COUNTRY = "Argentina";
  public static final byte PLAYER_AGE = 18;
  public static final long PLAYER_VALUE = 1_000_000L;
  public static final long OLD_TEAM_VALUE = 20_000_000L;
  public static final long NEW_TEAM_VALUE = 24_000_000L;
  public static final long OLD_TEAM_BUDGET = 5_000_000L;
  public static final long NEW_TEAM_BUDGET = 1_000_000L;
  public static final String OLD_TEAM_NAME = "TeamA";
  public static final String NEW_TEAM_NAME = "TeamA";
  public static final String OLD_TEAM_COUNTRY = "Colombia";
  public static final String NEW_TEAM_COUNTRY = "Argentina";
  public static final List<Player> TEAM_PLAYERS = new ArrayList<>();
  public static final List<PlayerEntity> TEAM_PLAYERS_ENTITIES = new ArrayList<>();

  public static final String USER_NAME_A = "bruce1";
  public static final String USER_PASSWORD_A = "tiger";
  public static final String USER_FIRST_NAME_A = "Bruce";
  public static final String USER_LAST_NAME_A = "Scott";
  public static final String USER_EMAIL_A = "bruce1@scott.db";

  public static final String USER_NAME_B = "frank1";
  public static final String USER_PASSWORD_B = "lion";
  public static final String USER_FIRST_NAME_B = "Frank";
  public static final String USER_LAST_NAME_B = "Dux";
  public static final String USER_EMAIL_B = "frank1@scott.db";

  public static final String CYPHERED_PASSWORD = "{bcrypt}$2a$10$Pb8XsVlOE493g6y//MFQ1OkiVjYQBT4VdkUk7s0b0vNI7m7LBliRG";
  public static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDEiLCJyb2xlcyI6WyJVU0VSIl0sImlzcyI6IkRyZWFtIFRlYW0gQVBJIiwiZXhwIjoxNjM5NDQ1NjA1LCJpYXQiOjE2Mzk0NDQ3MDV9.jFJV4vhRMllJf1SaR5An9GrSB10fX66GCYOJYbj0t3JDwSmN_b1DYrkseCJQgDbWj6pFF8gGV0gb19N99dAga-x6w3rK7bjL0zO7y03bhXYEWSOqCzSlw8FqiFGSTpcNqykU6hLFn8AuiAIGjT1Y9jyhPhbKlb7Lq7IhUcmJrMJsHkXXBlk5237NTtp_LZjK0Kl3gZm7vmkdYInliWbVsNr4ehc24vcfykMPMHgZugpSYyN62b4O58HWYHnBwuxYYWtkyRFyCl_z75K8GsOyuOZ80HqsjDHXMuK1v7LVlOgy5tJsnDypqJIBe1-hj-KWyvSyZnXpUQqTTGby2cRKcBGH0QYSWiy1pASGNjYPcqAHa2j4UFQwQFKSO6XNO6BKtQ0i6xiTgnF0tOKRK1Y4Orjegr6KmQvYom5ZX6rcTZniH86VSiQVTq4cAzKTzTsfguv_GGzwqfv3gDkwjhH1Vs1CDDXLLb5OXudnpu62o4PBPlUUKbSwE9ntj1aDWDdTsxl86Jsx3fMMOvkYHY9Bba8T82JNIlmFNQXF9sscBdUNyQx55UMLbEz7N72KI1DWgU2UU5Qh2KIhdkD7yL3CDL5-B7y7vBe3Etb1Sc4HZfAHNFjcFXK1elaIzZUFGaqswLswy8wRbYyU7Qa29pesGsKwmXNej8h6fpzoQZKy6hg";
  public static final String REFRESH_TOKEN = "26rvap1cr3maf85akd9jb27c7dlrlkfn82hn3rahrb9qhr0rcmtl82jjt75tapoqtqktkh6rgdicb7mm1i38qqhdpgb7v8q3omoffu7dj1o8is3h763o54l978tfp95v";


  public static User getTestUser(UUID userUUID, String username, String password, String firstname, String lastname, String email) {

    User user = new User();
    user.setId(userUUID);
    user.setUsername(username);
    user.setPassword(password);
    user.setFirstName(firstname);
    user.setLastName(lastname);
    user.setEmail(email);
    user.setRole(RoleEnum.USER);

    return user;
  }

  public static Team getTestTeam(UUID teamUUID, String teamName, long teamValue, String teamCountry, List<Player> players, long budget,
      User user) {

    Team team = new Team();
    team.setId(teamUUID);
    team.setName(teamName);
    team.setValue(teamValue);
    team.setCountry(teamCountry);
    team.setPlayers(players);
    team.setBudget(budget);
    team.setUser(user);

    return team;
  }


  public static Player getTestPlayer(UUID playerUUID, String country, String firstName, String lastName, long playerValue, byte age,
      Team team) {

    Player player = new Player();
    player.setId(playerUUID);
    player.setCountry(country);
    player.setFirstname(firstName);
    player.setLastname(lastName);
    player.setValue(playerValue);
    player.setAge(age);
    player.setType(PlayerType.ATTACKER);
    player.setTeam(team);

    return player;
  }

  public static UserEntity getTestUserEntity(User user) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(user.getId());
    userEntity.setUsername(user.getUsername());
    userEntity.setPassword(user.getPassword());
    userEntity.setFirstName(user.getFirstName());
    userEntity.setLastName(user.getLastName());
    userEntity.setEmail(user.getEmail());
    userEntity.setRole(RoleEnum.USER);

    return userEntity;
  }

  public static UserEntity getTestUserEntity(UUID userUUID, String username, String password, String firstname, String lastname,
      String email) {

    UserEntity user = new UserEntity();
    user.setId(userUUID);
    user.setUsername(username);
    user.setPassword(password);
    user.setFirstName(firstname);
    user.setLastName(lastname);
    user.setEmail(email);

    return user;
  }

  public static TeamEntity getTestTeamEntity(Team team, UserEntity userEntity) {
    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setName(team.getName());
    teamEntity.setValue(team.getValue());
    teamEntity.setCountry(team.getCountry());
    teamEntity.setBudget(team.getBudget());
    teamEntity.setPlayers(team.getPlayers().stream().map(player -> (PlayerEntity) Util.toEntity(player)).collect(Collectors.toList()));
    teamEntity.setUser(userEntity);

    return teamEntity;
  }

  public static TeamEntity getTestTeamEntity(UUID teamUUID, String teamName, long teamValue, String teamCountry, List<PlayerEntity> players,
      long budget, UserEntity user) {

    TeamEntity team = new TeamEntity();
    team.setId(teamUUID);
    team.setName(teamName);
    team.setValue(teamValue);
    team.setCountry(teamCountry);
    team.setPlayers(players);
    team.setBudget(budget);
    team.setUser(user);

    return team;
  }

  public static PlayerEntity getTestPlayerEntity(Player player, TeamEntity teamEntity) {

    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setFirstname(player.getFirstname());
    playerEntity.setLastname(player.getLastname());
    playerEntity.setValue(player.getValue());
    playerEntity.setCountry(player.getCountry());
    playerEntity.setType(player.getType());
    playerEntity.setTeam(teamEntity);

    return playerEntity;
  }

  public static PlayerEntity getTestPlayerEntity(UUID playerUUID, String country, String firstName, String lastName, long playerValue,
      byte age, TeamEntity team) {
    PlayerEntity player = new PlayerEntity();
    player.setId(playerUUID);
    player.setCountry(country);
    player.setFirstname(firstName);
    player.setLastname(lastName);
    player.setValue(playerValue);
    player.setAge(age);
    player.setType(PlayerType.ATTACKER);
    player.setTeam(team);

    return player;
  }

  public static TransferList getTransferList(UUID transferListID, Player player) {
    TransferList transferList = new TransferList();
    transferList.setId(transferListID);
    transferList.setPlayer(player);
    transferList.setValue(player.getValue());
    return transferList;
  }

  public static String getToken(JwtManager tokenManager, String role) {

    String username = "bruce1";
    String password = "tiger";

    return tokenManager.create(org.springframework.security.core.userdetails.User.builder()
        .username(username)
        .password(password)
        .authorities(role)
        .build());
  }
}
