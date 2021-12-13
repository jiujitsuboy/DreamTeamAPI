package com.toptal.dreamteamapi.repository;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<PlayerEntity, UUID> {

  Optional<PlayerEntity> findByFirstnameAndLastname(String firstname, String lastname);
}
