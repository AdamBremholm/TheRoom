package iths.theroom.repository;

import iths.theroom.entity.AvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<AvatarEntity, Long> {
    Optional<AvatarEntity> findByUuid(String uuid);
}
