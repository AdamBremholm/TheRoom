package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.RequestException;
import iths.theroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/theroom/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(path="s")
    public ResponseEntity getAll() {

        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping(path="/{name}")
    public ResponseEntity getOneByName(@PathVariable("name") String name){

        try {
            return ResponseEntity.ok(roomService.getOneByName(name));

        } catch (RequestException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .header("Content-Type", "application/json+problem")
                    .body(e.getResponseBody());
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity createRoom(@RequestBody RoomEntity roomEntity){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(roomEntity));

        } catch (RequestException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .header("Content-Type", "application/json+problem")
                    .body(e.getResponseBody());
        }
    }

    @PutMapping(path = "/{name}")
    public ResponseEntity updateRoom(@RequestBody RoomEntity roomEntity, @PathVariable("name") String name){

        try {
            return ResponseEntity.ok(roomService.updateRoom(name, roomEntity));

        } catch (RequestException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .header("Content-Type", "application/json+problem")
                    .body(e.getResponseBody());
        }
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity deleteRoom(@PathVariable("name") String name){

        try {
            return ResponseEntity.ok(roomService.deleteRoom(name));

        } catch (RequestException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .header("Content-Type", "application/json+problem")
                    .body(e.getResponseBody());
        }
    }
}
