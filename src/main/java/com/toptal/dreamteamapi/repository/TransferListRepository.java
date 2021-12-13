package com.toptal.dreamteamapi.repository;

import com.toptal.dreamteamapi.entity.PlayerEntity;
import com.toptal.dreamteamapi.entity.TransferListEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface TransferListRepository extends CrudRepository<TransferListEntity, UUID> {

  Optional<TransferListEntity> findByPlayerId(UUID playerId);
}
