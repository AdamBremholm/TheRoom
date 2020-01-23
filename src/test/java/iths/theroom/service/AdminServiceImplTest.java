package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AdminServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoomRepository roomRepository;

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
}
