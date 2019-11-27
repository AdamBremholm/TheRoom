package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomFactory roomFactory;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomFactory roomFactory, RoomRepository roomRepository) {
        this.roomFactory = roomFactory;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomModel> getAllRooms() {

        List<RoomEntity> roomEntities = roomRepository.findAll();
        return roomFactory.entityToModel(roomEntities);
    }
}
