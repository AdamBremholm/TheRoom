package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/theroom/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(path="/")
    public ResponseEntity getAll() {

        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping(path="/{name}")
    public ResponseEntity getOneByName(@PathVariable("name") String name) {

        return ResponseEntity.ok(roomService.getOneByName(name));
    }

    @PostMapping(path = "/")
    public ResponseEntity createRoom(@RequestBody RoomEntity roomEntity) {

        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(roomEntity));
    }

    @PutMapping(path = "/{name}")
    public ResponseEntity updateRoom(@RequestBody RoomEntity roomEntity, @PathVariable("name") String name) {

        return ResponseEntity.ok(roomService.updateRoom(name, roomEntity));
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity deleteRoom(@PathVariable("name") String name) {

        return ResponseEntity.ok(roomService.deleteRoom(name));
    }
}
