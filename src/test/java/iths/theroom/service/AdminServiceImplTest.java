package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;

public class AdminServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    private RoomFactory roomFactory;

    @InjectMocks
    private AdminServiceImpl adminService;

    private UserEntity userEntity;
    private RoomEntity roomEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userEntity = new UserEntity();
        userEntity.setUserName("hansen");
        userEntity.setFirstName("hans");
        userEntity.setLastName("hansen");
        userEntity.setEmail("hansen@test.com");
        userEntity.setPassword("hansen");
        userEntity.setPasswordConfirm("hansen");
        userEntity.setRoles("USER");

        roomEntity = new RoomEntity();
        roomEntity.setRoomName("roomy");
    }

    @Test
    public void banUserFromRoom() {
        Mockito.when(roomRepository.findRoomByNameWithQuery(roomEntity.getRoomName())).thenReturn(roomEntity);
        Mockito.when(userRepository.findUserByNameWithQuery(userEntity.getUserName())).thenReturn(userEntity);
        RoomModel roomModel = roomFactory.entityToModel(roomEntity);
        Mockito.when(adminService.banUserFromRoom("hansen", "roomy")).thenReturn(roomModel);
        assertThat(roomEntity.getBannedUsers().contains(userEntity));
    }
}
