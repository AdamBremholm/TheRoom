package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.exception.RequestException;
import iths.theroom.factory.EntityFactory;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.repository.RoomRepository;
import iths.theroom.service.helper.EntityValidator;
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
    private final EntityValidator<RoomEntity> entityValidator;

    @Autowired
    public RoomServiceImpl(RoomFactory entityFactory, RoomRepository roomRepository, EntityValidator<RoomEntity> entityValidator) {
        this.entityFactory = entityFactory;
        this.roomRepository = roomRepository;
        this.entityValidator = entityValidator;
    }

    @Override
    public List<RoomModel> getAllRooms() {

        List<RoomEntity> roomEntities = roomRepository.findAll();
        return entityFactory.entityToModel(roomEntities);

    }

    @Override
    public RoomModel getOneByName(String name) throws RequestException {

        if(name == null){
            throw new BadRequestException("Missing critical path parameter: 'name'");
        }

        RoomEntity roomEntity = checkIfRoomExists(name);

        return entityFactory.entityToModel(roomEntity);
    }

    @Override
    public RoomModel save(RoomEntity roomEntity) throws RequestException {

        entityValidator.validate(roomEntity);
        if(roomRepository.exists(Example.of(roomEntity))){
            throw new BadRequestException("Room with name '" + roomEntity.getRoomName() + "' already exists.");
        }

        try{
            RoomEntity savedRoomEntity = roomRepository.saveAndFlush(roomEntity);
            return entityFactory.entityToModel(savedRoomEntity);

        } catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public RoomModel updateRoom(String name, RoomEntity roomEntity) throws RequestException {

        RoomEntity roomEntityToUpdate = checkIfRoomExists(name);

        roomEntityToUpdate.setRoomName(roomEntity.getRoomName());

        try {
            RoomEntity updatedRoomEntity = roomRepository.saveAndFlush(roomEntityToUpdate);
            return entityFactory.entityToModel(updatedRoomEntity);

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public RoomModel deleteRoom(String name) throws RequestException {

        RoomEntity roomToDelete = checkIfRoomExists(name);

        try {
            roomRepository.delete(roomToDelete);

        } catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

        return entityFactory.entityToModel(roomToDelete);
    }

    private RoomEntity checkIfRoomExists(String name) throws RequestException {

        RoomEntity roomEntityFound = roomRepository.getOneByRoomName(name);

        if(roomEntityFound != null){
            return roomEntityFound;

        } else {
            throw new NotFoundException("Room with name '" + name + "' not found.");
        }
    }
}
