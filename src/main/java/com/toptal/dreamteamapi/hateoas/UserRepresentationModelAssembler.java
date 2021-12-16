package com.toptal.dreamteamapi.hateoas;

import com.toptal.dreamteamapi.controller.AuthController;
import com.toptal.dreamteamapi.controller.TeamController;
import com.toptal.dreamteamapi.entity.UserEntity;
import com.toptal.dreamteamapi.model.SignInReq;
import com.toptal.dreamteamapi.model.SignedInUser;
import com.toptal.dreamteamapi.model.User;
import com.toptal.dreamteamapi.service.UserService;
import com.toptal.dreamteamapi.service.impl.UserServiceImpl;
import com.toptal.dreamteamapi.service.Util;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserRepresentationModelAssembler extends RepresentationModelAssemblerSupport<UserEntity, User> {

  private UserService userService;
  public UserRepresentationModelAssembler(UserService userService) {
    super(AuthController.class, User.class);
    this.userService = userService;
  }

  @Override
  public User toModel(UserEntity entity) {
    User user = (User)Util.toModel(entity);
    SignInReq signInReq = new SignInReq(user.getUsername(),user.getPassword());
    user.setPassword("Ciphered...");
    user.add(linkTo(methodOn(AuthController.class).signIn(signInReq)).withRel("user-signin"));
    return user;
  }

  public SignedInUser toModel(SignedInUser model) {
    UserEntity userEntity =  userService.findUserByUserName(model.getUserName()).get();
    return model.add(linkTo(methodOn(TeamController.class).getTeam(userEntity.getId().toString())).withRel("user-team"));
  }


}
