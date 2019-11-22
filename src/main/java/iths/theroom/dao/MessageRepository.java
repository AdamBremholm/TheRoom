package iths.theroom.dao;

import iths.theroom.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByUuid(String uuid);
}
