package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.model.RoomModel;
import iths.theroom.service.RoomService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ActiveProfiles("test")
public class RoomControllerIntegrationTest {

    @Mock
    RoomService roomService;

    @InjectMocks
    RoomController roomController;

    private RoomEntity roomEntity;
    private RoomModel roomModel1;
    private RoomModel roomModel2;
    private List<RoomModel> roomModels;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        roomEntity = new RoomEntity();
        roomModel1 = new RoomModel();
        roomModel2 = new RoomModel();
        roomModel1.setRoomName("RoomName1");
        roomModel2.setRoomName("RoomName2");

        roomModels = new ArrayList<>();
        roomModels.add(roomModel1);
        roomModels.add(roomModel2);
    }

    @Test
    public void whenGetAll_ReturnRoomModelList(){

        int expectedListSize = roomModels.size();

        Mockito.when(roomService.getAllRooms()).thenReturn(roomModels);

        List<RoomModel> result = roomController.getAll();

        assertNotNull(result);
        int actualListSize = result.size();
        assertEquals(expectedListSize, actualListSize);
    }

    @Test
    public void whenGetOneByName_ReturnRoomModel() {

        String expectedRoomName = roomModel1.getRoomName();

        Mockito.when(roomService.getOneModelByName("")).thenReturn(roomModel1);

        RoomModel result = roomController.getOneByName("");

        Assert.assertNotNull(result);
        String actualRoomName = result.getRoomName();

        Assert.assertEquals(expectedRoomName, actualRoomName);
    }


    @Test
    public void whenCreateRoom_ReturnRoomModel() {

        String expectedRoomName = roomModel1.getRoomName();

        Mockito.when(roomService.save(roomEntity)).thenReturn(roomModel1);

        RoomModel result = roomController.createRoom(roomEntity);

        Assert.assertNotNull(result);
        String actualRoomName = result.getRoomName();

        Assert.assertEquals(expectedRoomName, actualRoomName);
    }

    @Test
    public void whenUpdateRoom_ReturnRoomModel(){

        String expectedRoomName = roomModel1.getRoomName();

        Mockito.when(roomService.updateRoom("", roomEntity)).thenReturn(roomModel1);

        RoomModel result = roomController.updateRoom(roomEntity, "");

        Assert.assertNotNull(result);
        String actualRoomName = result.getRoomName();

        Assert.assertEquals(expectedRoomName, actualRoomName);
    }

    @Test
    public void whenDeleteRoom_ReturnRoomModel(){

        String expectedRoomName = roomModel2.getRoomName();

        Mockito.when(roomService.deleteRoom("")).thenReturn(roomModel2);

        RoomModel result = roomController.deleteRoom("");

        Assert.assertNotNull(result);
        String actualRoomName = result.getRoomName();

        Assert.assertEquals(expectedRoomName, actualRoomName);
    }

}
