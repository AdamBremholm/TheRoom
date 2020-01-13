package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.repository.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoomServiceImplTest {

    @Mock
    RoomFactory roomFactory;

    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    RoomServiceImpl roomService;

    private RoomEntity roomEntity1;
    private RoomEntity roomEntity2;
    private RoomModel roomModel1;
    private RoomModel roomModel2;
    private List<RoomModel> roomModels;

    private List<RoomEntity> roomEntities;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        roomEntity1 = new RoomEntity();
        roomEntity2 = new RoomEntity();
        roomEntity1.setRoomName("RoomName1");
        roomEntity2.setRoomName("RoomName2");

        roomModel1 = new RoomModel();
        roomModel2 = new RoomModel();
        roomModel1.setRoomName("RoomName1");
        roomModel2.setRoomName("RoomName2");

        roomModels = new ArrayList<>();
        roomModels.add(roomModel1);
        roomModels.add(roomModel2);

        roomEntities = new ArrayList<>();
        roomEntities.add(roomEntity1);
        roomEntities.add(roomEntity2);
    }

    @Test
    public void whenGetAllRooms_ReturnListWithAllRooms(){

        when(roomRepository.findAll()).thenReturn(roomEntities);
        when(roomFactory.entityToModel(roomEntities)).thenReturn(roomModels);

        List<RoomModel> resultList = roomService.getAllRooms();

        assertNotNull(resultList);
        assertEquals(roomModels, resultList);
    }

    @Test
    public void whenGetOneByName_ReturnRoomModel(){

        String expectedRoomName = roomModel1.getRoomName();

        when(roomRepository.getOneByRoomName(expectedRoomName)).thenReturn(Optional.of(roomEntity1));
        when(roomFactory.entityToModel(roomEntity1)).thenReturn(roomModel1);

        RoomModel result = roomService.getOneByName(expectedRoomName);

        assertNotNull(result);

        String actualRoomName = result.getRoomName();

        assertEquals(expectedRoomName, actualRoomName);
    }
}
