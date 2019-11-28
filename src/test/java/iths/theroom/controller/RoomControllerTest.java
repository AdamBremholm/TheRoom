package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.RequestException;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RoomControllerTest {

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

        ResponseEntity result = roomController.getAll();

        List resultList = (List) result.getBody();
        assertNotNull(resultList);
        int actualListSize = resultList.size();
        int actualStatus = result.getStatusCode().value();

        assertEquals(expectedListSize, actualListSize);
        assertEquals(STATUS_OK, actualStatus);
    }

    @Test
    public void whenGetOneByName_ReturnRoomModelInResponseBodyAndStatusOK() throws RequestException {

        String expectedRoomName = roomModel1.getRoomName();

        Mockito.when(roomService.getOneByName("")).thenReturn(roomModel1);

        ResponseEntity result = roomController.getOneByName("");

        RoomModel resultModel = (RoomModel) result.getBody();
        Assert.assertNotNull(resultModel);
        String actualRoomName = resultModel.getRoomName();
        int actualStatus = result.getStatusCode().value();

        Assert.assertEquals(expectedRoomName, actualRoomName);
        assertEquals(STATUS_OK, actualStatus);
    }

    @Test
    public void whenGetOneByNameInvalidName_ReturnErrorInResponseBodyAndStatusBadRequest() throws RequestException {

        String expectedErrorDetails = "12345";

        Mockito.when(roomService.getOneByName(null)).thenThrow(new BadRequestException(expectedErrorDetails));

        ResponseEntity result = roomController.getOneByName(null);

        String actualErrorDetails = (String) result.getBody();
        Assert.assertNotNull(actualErrorDetails);

        int actualStatus = result.getStatusCode().value();

        Assert.assertEquals(expectedRoomName, actualRoomName);
        assertEquals(STATUS_BAD, actualStatus);
    }

    @Test
    public void whenCreateRoom_ReturnRoomModelInResponseBodyAndStatusCreated() throws RequestException {

        String expectedRoomName = roomModel1.getRoomName();

        Mockito.when(roomService.save(roomEntity)).thenReturn(roomModel1);

        ResponseEntity result = roomController.createRoom(roomEntity);

        RoomModel resultModel = (RoomModel) result.getBody();
        Assert.assertNotNull(resultModel);
        String actualRoomName = resultModel.getRoomName();
        int actualStatus = result.getStatusCode().value();

        Assert.assertEquals(expectedRoomName, actualRoomName);
        assertEquals(STATUS_CREATED, actualStatus);
    }

}
