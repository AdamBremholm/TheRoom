package iths.theroom.repository;

import iths.theroom.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> getOneByRoomName(String roomName);
    @Query(value = "SELECT * FROM room WHERE room_name = :roomname", nativeQuery = true)
    RoomEntity getRoomByNameWithQuery(@Param("roomname") String roomName);

}
