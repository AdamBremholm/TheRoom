package iths.theroom.repository;

import iths.theroom.entity.AvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<AvatarEntity, Long> {
}
