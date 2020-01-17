package iths.theroom.repository;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    Optional<MessageEntity> findByUuid(String uuid);
    List<MessageEntity> findAllByRoomEntityOrderById(RoomEntity roomEntity);
}
