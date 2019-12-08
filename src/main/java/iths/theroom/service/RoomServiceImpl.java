package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.factory.EntityFactory;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final EntityFactory<RoomModel, RoomEntity> entityFactory;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomFactory entityFactory, RoomRepository roomRepository) {
        this.entityFactory = entityFactory;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomModel> getAllRooms() {

        List<RoomEntity> roomEntities = roomRepository.findAll();
        return entityFactory.entityToModel(roomEntities);

    }

    @Override
    public RoomModel getOneByName(String name) {

        if(name == null){
            throw new BadRequestException("Missing critical path parameter: 'name'");
        }

        RoomEntity roomEntity = checkIfRoomExists(name);

        return entityFactory.entityToModel(roomEntity);
    }


    @Override
    public RoomEntity getOneByNameE(String name) {

        if(name == null){
            throw new BadRequestException("Missing critical path parameter: 'name'");
        }

        RoomEntity roomEntity = checkIfRoomExists(name);

        return roomEntity;
    }

    @Override
    public RoomModel save(RoomEntity roomEntity) {

        validate(roomEntity);

        RoomEntity savedRoomEntity = roomRepository.saveAndFlush(roomEntity);
        return entityFactory.entityToModel(savedRoomEntity);
    }

    @Override
    public RoomModel updateRoom(String name, RoomEntity roomEntity) {

        RoomEntity roomEntityToUpdate = checkIfRoomExists(name);

        roomEntityToUpdate.setRoomName(roomEntity.getRoomName());

        RoomEntity updatedRoomEntity = roomRepository.saveAndFlush(roomEntityToUpdate);
        return entityFactory.entityToModel(updatedRoomEntity);

    }

    @Override
    public RoomModel deleteRoom(String name) {

        RoomEntity roomToDelete = checkIfRoomExists(name);

        try {
            roomRepository.delete(roomToDelete);

        } catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

        return entityFactory.entityToModel(roomToDelete);
    }

    private RoomEntity checkIfRoomExists(String name) {

        RoomEntity roomEntityFound = roomRepository.getOneByRoomName(name);

        if(roomEntityFound != null){
            return roomEntityFound;

        } else {
            throw new NotFoundException("Room with name '" + name + "' not found.");
        }
    }

    private void validate(RoomEntity roomEntity) throws BadRequestException {

        if(roomEntity.getRoomName() == null) {
            throw new BadRequestException("Missing critical field: roomName");
        }

        if(roomRepository.exists(Example.of(roomEntity))) {
            throw new BadRequestException("Room with name '" + roomEntity.getRoomName() + "' already exists.");
        }
    }
}
