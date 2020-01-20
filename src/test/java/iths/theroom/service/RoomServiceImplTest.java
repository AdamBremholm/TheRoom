package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.pojos.MessageForm;
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

    @Mock
    UserServiceImpl userService;

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
        roomEntity1.setRoomName("RoomName1");
        roomEntity1.setBackgroundColor("Blue");

        roomEntity2 = new RoomEntity();
        roomEntity2.setRoomName("RoomName2");
        roomEntity2.setBackgroundColor("Red");

        roomModel1 = new RoomModel();
        roomModel1.setRoomName("RoomName1");

        roomModel2 = new RoomModel();
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
    public void whenGetOneModelByName_ReturnRoomModel(){

        String expectedRoomName = roomModel1.getRoomName();

        when(roomRepository.getOneByRoomName(expectedRoomName)).thenReturn(Optional.of(roomEntity1));
        when(roomFactory.entityToModel(roomEntity1)).thenReturn(roomModel1);

        RoomModel result = roomService.getOneModelByName(expectedRoomName);
        assertNotNull(result);

        String actualRoomName = result.getRoomName();
        assertEquals(expectedRoomName, actualRoomName);
    }

    @Test(expected = BadRequestException.class)
    public void whenGetOneModelByName_IfNameIsNullThrowBadRequestException(){

        roomService.getOneModelByName(null);
    }

    @Test(expected = NotFoundException.class)
    public void whenGetOneModelByName_IfRoomDoesntExistThrowNotFoundException(){

        when(roomRepository.getOneByRoomName("")).thenReturn(Optional.empty());
        roomService.getOneModelByName("");
    }

    @Test
    public void whenGetOneEntityByName_ReturnRoomEntity(){

        String expectedRoomName = roomEntity1.getRoomName();

        when(roomRepository.getOneByRoomName(expectedRoomName)).thenReturn(Optional.of(roomEntity1));

        RoomEntity result = roomService.getOneEntityByName(expectedRoomName);
        assertNotNull(result);

        String actualRoomName = result.getRoomName();
        assertEquals(expectedRoomName, actualRoomName);
    }

    @Test(expected = BadRequestException.class)
    public void whenGetOneEntityByName_IfNameIsNullThrowBadRequestException(){

        roomService.getOneEntityByName(null);
    }

    @Test(expected = NotFoundException.class)
    public void whenGetOneEntityByName_IfRoomDoesntExistThrowNotFoundException(){

        when(roomRepository.getOneByRoomName("")).thenReturn(Optional.empty());
        roomService.getOneEntityByName("");
    }

    @Test
    public void whenSave_IfRoomExistsOnlyReturnRoomModel(){

        String expectedRoomName = roomModel1.getRoomName();
        when(roomRepository.getOneByRoomName(roomEntity1.getRoomName())).thenReturn(Optional.of(roomEntity1));
        when(roomFactory.entityToModel(roomEntity1)).thenReturn(roomModel1);

        RoomModel result = roomService.save(roomEntity1);
        assertNotNull(result);
        verify(roomRepository, times(0)).saveAndFlush(roomEntity1);

        String actualRoomName = result.getRoomName();
        assertEquals(expectedRoomName, actualRoomName);
    }

    @Test
    public void whenSave_IfRoomDoesntExistSaveAndReturnRoomModel(){

        String expectedRoomName = roomModel1.getRoomName();
        when(roomRepository.getOneByRoomName(roomEntity1.getRoomName())).thenReturn(Optional.empty());
        when(roomRepository.saveAndFlush(roomEntity1)).thenReturn(roomEntity1);
        when(roomFactory.entityToModel(roomEntity1)).thenReturn(roomModel1);

        RoomModel result = roomService.save(roomEntity1);
        assertNotNull(result);
        verify(roomRepository, times(1)).saveAndFlush(roomEntity1);

        String actualRoomName = result.getRoomName();
        assertEquals(expectedRoomName, actualRoomName);
    }

    @Test(expected = BadRequestException.class)
    public void whenSave_IfRoomNameIsNullThrowBadRequestException(){

        RoomEntity roomEntity = new RoomEntity();
        roomService.save(roomEntity);
    }

    @Test
    public void whenUpdateRoom_IfRoomExistSaveWithNewDataAndReturnRoomModel(){

        String expectedRoomNameAfterUpdate = roomEntity2.getRoomName();
        String expectedRoomColorAfterUpdate = roomEntity2.getBackgroundColor();

        when(roomRepository.getOneByRoomName(roomEntity1.getRoomName())).thenReturn(Optional.of(roomEntity1));
        when(roomRepository.saveAndFlush(roomEntity1)).thenReturn(roomEntity1);
        when(roomFactory.entityToModel(roomEntity1)).thenReturn(roomModel1);

        RoomModel roomModel = roomService.updateRoom(roomEntity1.getRoomName(), roomEntity2);

        assertNotNull(roomModel);
        verify(roomRepository, times(1)).saveAndFlush(roomEntity1);

        String actualRoomNameAfterUpdate = roomEntity1.getRoomName();
        String actualRoomColorAfterUpdate = roomEntity1.getBackgroundColor();
        assertEquals(expectedRoomNameAfterUpdate, actualRoomNameAfterUpdate);
        assertEquals(expectedRoomColorAfterUpdate, actualRoomColorAfterUpdate);
    }

    @Test(expected = NotFoundException.class)
    public void whenUpdateRoom_IfRoomDoesntExistThrowNotFoundException(){

        when(roomRepository.getOneByRoomName("")).thenReturn(Optional.empty());
        roomService.updateRoom("", roomEntity1);
    }

    @Test
    public void whenUpdateRoomMessageForm_SaveNewDataAndReturnRoomModel(){

        String expectedRoomNameAfterUpdate = "roomEntity1";
        String expectedRoomColorAfterUpdate = "Green";

        MessageForm messageForm = new MessageForm();
        messageForm.setRoomName(expectedRoomNameAfterUpdate);
        messageForm.setRoomBackgroundColor(expectedRoomColorAfterUpdate);

        when(roomRepository.getOneByRoomName(expectedRoomNameAfterUpdate)).thenReturn(Optional.of(roomEntity1));
        when(roomRepository.saveAndFlush(roomEntity1)).thenReturn(roomEntity1);
        when(roomFactory.entityToModel(roomEntity1)).thenReturn(roomModel1);

        RoomModel roomModel = roomService.updateRoom(messageForm);
        assertNotNull(roomModel);
        verify(roomRepository, times(1)).saveAndFlush(roomEntity1);

        String actualRoomNameAfterUpdate = roomEntity1.getRoomName();
        String actualRoomColorAfterUpdate = roomEntity1.getBackgroundColor();

        assertEquals(expectedRoomNameAfterUpdate, actualRoomNameAfterUpdate);
        assertEquals(expectedRoomColorAfterUpdate, actualRoomColorAfterUpdate);
    }

    @Test(expected = NotFoundException.class)
    public void whenUpdateRoomMessageForm_IfRoomDoesntExistThrowNotFoundException(){

        MessageForm messageForm = new MessageForm();
        messageForm.setRoomName("");

        when(roomRepository.getOneByRoomName("")).thenReturn(Optional.empty());
        roomService.updateRoom(messageForm);
    }

    @Test
    public void whenDeleteRoom_DeleteAndReturnRoomModel(){

        when(roomRepository.getOneByRoomName("")).thenReturn(Optional.of(roomEntity1));
        when(roomFactory.entityToModel(roomEntity1)).thenReturn(roomModel1);

        RoomModel roomModel = roomService.deleteRoom("");
        assertNotNull(roomModel);
        verify(roomRepository, times(1)).delete(roomEntity1);
    }

    @Test(expected = NotFoundException.class)
    public void whenDeleteRoom_IfRoomNotExistThrowNotFoundException(){

        when(roomRepository.getOneByRoomName("")).thenReturn(Optional.empty());
        roomService.deleteRoom("");
    }

    @Test(expected = BadRequestException.class)
    public void whenDeleteRoom_IfDeleteFailsThrowBadRequestException(){

        when(roomRepository.getOneByRoomName("")).thenReturn(Optional.of(roomEntity1));
        doThrow(new RuntimeException()).when(roomRepository).delete(roomEntity1);

        roomService.deleteRoom("");
    }

    @Test
    public void whenIsUserBanned_IfTrueReturnTrue(){
        UserEntity user = new UserEntity("abc");
        RoomEntity room = new RoomEntity("123");
        room.banUser(user);
        when(roomRepository.getOneByRoomName(any())).thenReturn(Optional.of(room));
        when(userService.getByUserName(any())).thenReturn(user);
        assertTrue(roomService.isUserBannedHere(user.getUserName(), room.getRoomName()));
    }

    @Test
    public void whenIsUserBanned_IfFalseReturnFalse(){
        UserEntity user = new UserEntity("abc");
        RoomEntity room = new RoomEntity("123");
        when(roomRepository.getOneByRoomName(any())).thenReturn(Optional.of(room));
        when(userService.getByUserName(any())).thenReturn(user);
        assertFalse(roomService.isUserBannedHere(user.getUserName(), room.getRoomName()));
    }

    @Test(expected = NoSuchUserException.class)
    public void whenIsUserBanned_UserDoesntExist(){
        RoomEntity room = new RoomEntity("123");
        when(roomRepository.getOneByRoomName(any())).thenReturn(Optional.of(room));
        when(userService.getByUserName("this name doesnt exist")).thenThrow(new NoSuchUserException());
        roomService.isUserBannedHere("this name doesnt exist", room.getRoomName());
    }

}
