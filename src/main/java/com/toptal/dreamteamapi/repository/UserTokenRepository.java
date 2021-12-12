package com.toptal.dreamteamapi.repository;

import com.toptal.dreamteamapi.entity.UserTokenEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserTokenRepository extends CrudRepository<UserTokenEntity, UUID> {

  Optional<UserTokenEntity> findByRefreshToken(String refreshToken);
  Optional<UserTokenEntity> deleteByUserId(UUID userId);

}

