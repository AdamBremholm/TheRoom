package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.model.RoomModel;
import iths.theroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(path="/")
    public List<RoomModel> getAll() {

        return roomService.getAllRooms();
    }

    @GetMapping(path="/{name}")
    public RoomModel getOneByName(@PathVariable("name") String name) {

        return roomService.getOneModelByName(name);
    }

    @PostMapping(path = "/")
    public RoomModel createRoom(@RequestBody RoomEntity roomEntity) {

        return roomService.save(roomEntity);
    }

    @PutMapping(path = "/{name}")
    public RoomModel updateRoom(@RequestBody RoomEntity roomEntity, @PathVariable("name") String name) {

        return roomService.updateRoom(name, roomEntity);
    }

    @DeleteMapping(path = "/{name}")
    public RoomModel deleteRoom(@PathVariable("name") String name) {

        return roomService.deleteRoom(name);
    }
}
