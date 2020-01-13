package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.repository.RoomRepository;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class RoomServiceImplTest {

    @Mock
    RoomFactory roomFactory;

    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    RoomServiceImpl roomService;

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
}
