package iths.theroom.repository;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    Optional<MessageEntity> findByUuid(String uuid);
    List<MessageEntity> findAllByRoomEntityOrderById(RoomEntity roomEntity);
    List<MessageEntity> findAllBySenderOrderByTimeDesc(UserEntity userEntity);
    List<MessageEntity> findAllBySenderAndRoomEntityOrderByTimeDesc(UserEntity userEntity, RoomEntity roomEntity);
}
