package iths.theroom.service.helper;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator implements EntityValidator<RoomEntity> {

    @Override
    public void validate(RoomEntity roomEntity) throws BadRequestException {
        if(roomEntity.getRoomName() == null){
            throw new BadRequestException("Missing critical field: roomName");
        }
    }
}
