package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.model.RoomModel;
import iths.theroom.service.RoomService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@ActiveProfiles("test")
public class RoomControllerIntegrationTest {

    @Mock
    RoomService roomService;

    @InjectMocks
    RoomController roomController;

    private static final int STATUS_OK = 200;
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_BAD_REQUEST = 400;

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
    public void whenGetAll_ReturnRoomModelListInResponseBodyAndStatusOK(){

        int expectedListSize = roomModels.size();

        Mockito.when(roomService.getAllRooms()).thenReturn(roomModels);

        List<RoomModel> result = roomController.getAll();

        assertNotNull(result);
        int actualListSize = result.size();
        assertEquals(expectedListSize, actualListSize);

    }

    @Test
    public void whenGetOneByName_ReturnRoomModelInResponseBodyAndStatusOK() {

        String expectedRoomName = roomModel1.getRoomName();

        Mockito.when(roomService.getOneByName("")).thenReturn(roomModel1);

        RoomModel result = roomController.getOneByName("");

        Assert.assertNotNull(result);
        String actualRoomName = result.getRoomName();

        Assert.assertEquals(expectedRoomName, actualRoomName);
    }

    @Test
    public void whenGetOneByNameInvalidName_ReturnErrorInResponseBodyAndStatusBadRequest() {

        String expectedErrorDetails = "12345abc";

        Mockito.when(roomService.getOneByName(null)).thenThrow(new BadRequestException(expectedErrorDetails));

        try{
            roomController.getOneByName(null);
        } catch (BadRequestException e){
            assertTrue(e.getMessage().contains(expectedErrorDetails));
        }

    }

    @Test
    public void whenCreateRoom_ReturnRoomModelInResponseBodyAndStatusCreated() {

        String expectedRoomName = roomModel1.getRoomName();

        Mockito.when(roomService.save(roomEntity)).thenReturn(roomModel1);

        RoomModel result = roomController.createRoom(roomEntity);

        Assert.assertNotNull(result);
        String actualRoomName = result.getRoomName();

        Assert.assertEquals(expectedRoomName, actualRoomName);
    }

}
