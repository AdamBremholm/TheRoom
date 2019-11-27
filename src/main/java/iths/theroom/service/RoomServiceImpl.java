package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.exception.RequestException;
import iths.theroom.factory.EntityFactory;
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
    public RoomModel getOneByName(String name) throws RequestException {

        if(name == null){
            throw new BadRequestException("Missing critical path parameter: 'Name'");
        }

        RoomEntity roomEntity = checkIfRoomExists(name);

        return entityFactory.entityToModel(roomEntity);
    }

    @Override
    public RoomModel save(RoomEntity roomEntity) throws RequestException {

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
