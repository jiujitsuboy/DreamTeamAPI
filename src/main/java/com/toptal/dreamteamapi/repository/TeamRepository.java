package com.toptal.dreamteamapi.repository;

import com.toptal.dreamteamapi.entity.TeamEntity;
import com.toptal.dreamteamapi.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<TeamEntity, UUID> {

  Optional<TeamEntity> findByUser(User user);
  Optional<TeamEntity> findByUserId(UUID userId);
}
