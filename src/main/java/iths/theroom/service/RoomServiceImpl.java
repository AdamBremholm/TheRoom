package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final UserService userService;

    private final RoomFactory roomFactory;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomFactory roomFactory, RoomRepository roomRepository, UserService userService) {
        this.roomFactory = roomFactory;
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

    @Override
    public List<RoomModel> getAllRooms() {

        List<RoomEntity> roomEntities = roomRepository.findAll();
        return roomFactory.entityToModel(roomEntities);
    }

    @Override
    public RoomModel getOneModelByName(String name) {

        if(name == null){
            throw new BadRequestException("Missing critical path parameter: 'name'");
        }

        RoomEntity roomEntity = checkIfRoomExists(name);

        return roomFactory.entityToModel(roomEntity);
    }

    @Override
    public RoomEntity getOneEntityByName(String name) {

        if(name == null){
            throw new BadRequestException("Missing critical path parameter: 'name'");
        }

        return checkIfRoomExists(name);
    }

    @Override
    public RoomModel save(RoomEntity roomEntity) {

        if(roomEntity.getRoomName() == null) {
            throw new BadRequestException("Missing critical field: roomName");
        }

        Optional<RoomEntity> optionalRoomEntity = roomRepository.getOneByRoomName(roomEntity.getRoomName());

        if(optionalRoomEntity.isEmpty()) {
            RoomEntity savedRoomEntity = roomRepository.saveAndFlush(roomEntity);
            return roomFactory.entityToModel(savedRoomEntity);

        } else {
            return roomFactory.entityToModel(optionalRoomEntity.get());
        }
    }

    @Override
    public RoomModel updateRoom(String name, RoomEntity roomEntity) {

        RoomEntity roomEntityToUpdate = checkIfRoomExists(name);

        roomEntityToUpdate.setRoomName(roomEntity.getRoomName());
        roomEntityToUpdate.setBackgroundColor(roomEntity.getBackgroundColor());

        RoomEntity updatedRoomEntity = roomRepository.saveAndFlush(roomEntityToUpdate);
        return roomFactory.entityToModel(updatedRoomEntity);
    }

    @Override
    public RoomModel updateRoom(MessageForm messageForm){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomName(messageForm.getRoomName());
        roomEntity.setBackgroundColor(messageForm.getRoomBackgroundColor());
        return updateRoom(roomEntity.getRoomName(), roomEntity);
    }

    @Override
    public boolean isUserBannedHere(String username, String roomName) {
        try {
            UserEntity user = userService.getByUserName(username);
            RoomEntity room = getOneEntityByName(roomName);
            return room.getBannedUsers().contains(user);
        }
        catch(Exception e){}
        return false;
    }

    @Override
    public RoomModel deleteRoom(String name) {

        RoomEntity roomToDelete = checkIfRoomExists(name);

        try {
            roomRepository.delete(roomToDelete);

        } catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

        return roomFactory.entityToModel(roomToDelete);
    }

    private RoomEntity checkIfRoomExists(String name) {

        Optional<RoomEntity> roomEntityFound = roomRepository.getOneByRoomName(name);

        if(roomEntityFound.isPresent()){
            return roomEntityFound.get();

        } else {
            throw new NotFoundException("Room with name '" + name + "' not found.");
        }
    }

}
