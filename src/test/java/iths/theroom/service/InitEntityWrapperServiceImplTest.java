package iths.theroom.service;

import com.sun.security.auth.UserPrincipal;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.*;
import iths.theroom.model.RoomModel;
import iths.theroom.pojos.MessageForm;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class InitEntityWrapperServiceImplTest {

    @Mock
    UserServiceImpl userService;

    @Mock
    RoomService roomService;



    @InjectMocks
    InitEntityWrapperServiceImpl initEntityWrapperService;


    @Before
    public void setUp() {

    }


    @Test(expected = UnauthorizedException.class)
    public void initSession_() {
        when(userService.save(any())).thenReturn(new UserEntity("erik"));
        when(roomService.save(any())).thenReturn(new RoomModel());


        MessageForm form = new MessageForm();
        form.setRoomName("myroom");
        form.setSender("erik");


        initEntityWrapperService.initRoomSession(form);
    }


}
