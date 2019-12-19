package iths.theroom.repository;

import iths.theroom.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> getOneByRoomName(String roomName);

}
