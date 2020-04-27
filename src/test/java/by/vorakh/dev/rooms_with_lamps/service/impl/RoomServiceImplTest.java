package by.vorakh.dev.rooms_with_lamps.service.impl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import by.vorakh.dev.rooms_with_lamps.exception.CreatingException;
import by.vorakh.dev.rooms_with_lamps.model.form.RoomForm;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomWithLampID;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import by.vorakh.dev.rooms_with_lamps.exception.NonexistentEntityException;
import by.vorakh.dev.rooms_with_lamps.exception.RoomAccessException;
import by.vorakh.dev.rooms_with_lamps.repository.LampRepository;
import by.vorakh.dev.rooms_with_lamps.repository.RoomRepository;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;

public class RoomServiceImplTest {
    
    @Mock
    private LampRepository lampRepository;
    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private RoomServiceImpl service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRoomIntegerString_Room_Exists() {
        Integer roomId = 3;
        String roomName = "Test room";
        Integer lampId = 4;
        Optional<RoomWithLampID> expected = Optional.of(new RoomWithLampID(roomId, roomName, lampId));
        String countryCode = "UK";
        boolean isLightsOn = true;
        Lamp lamp = new Lamp(lampId, isLightsOn);
        Room room = new Room(roomId, roomName, countryCode, lamp);
        Optional<Room> returnedRoom = Optional.of(room);
        when(roomRepository.getRoom(roomId,countryCode)).thenReturn(returnedRoom);
        Optional<RoomWithLampID> actual = service.getRoom(roomId, countryCode);
        assertEquals(expected, actual);
    }
    
    @Test(expected = RoomAccessException.class)
    public void testGetRoomIntegerString_Room_Exists_Without_Lamp_ThrownException() {
        Optional<RoomWithLampID> expected = Optional.empty();
        Integer roomId = 3;
        String countryCode = "UK";
        Optional<Room> emptyRoom = Optional.empty();
        when(roomRepository.getRoom(roomId,countryCode)).thenReturn(emptyRoom);
        Optional<RoomWithLampID> actual = service.getRoom(roomId, countryCode);
        assertEquals(expected, actual);
    }
    
    @Test(expected = NonexistentEntityException.class)
    public void testGetRoomIntegerString_Room_Not_Exists_ThrownException() {
        Optional<RoomWithLampID> expected = Optional.empty();
        Integer roomId = 3;
        String roomName = "Test room";
        String countryCode = "UK";
        Lamp lamp = null;
        Room roomWithoutLamp = new Room(roomId, roomName, countryCode, lamp);
        Optional<Room> returnedRoomWithoutLamp = Optional.of(roomWithoutLamp);
        when(roomRepository.getRoom(roomId,countryCode)).thenReturn(returnedRoomWithoutLamp);
        Optional<RoomWithLampID> actual = service.getRoom(roomId, countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateRoom_Room_Is_Unique() {
        Optional<Integer> expected = Optional.of(1);
        String roomName = "Test room";
        String countryCode = "UK";
        RoomForm newRoomForm = new RoomForm(roomName, countryCode);
        Answer<Boolean> returnFalse = new Answer<Boolean>(){
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return false;
            }
        };
        doAnswer(returnFalse).when(roomRepository).isContainRoom(newRoomForm.getName());
        when(roomRepository.createRoom(any(Room.class))).thenReturn(Optional.of(1));
        Optional<Integer> actual = service.createRoom(newRoomForm);
        assertEquals(expected, actual);
        verify(roomRepository, times(1)).createRoom(any(Room.class));
        verify(lampRepository, times(1)).createLamp(any(Lamp.class));
        verify(roomRepository, times(1)).updateRoom(any(Room.class));
    }

    @Test(expected = CreatingException.class)
    public void testCreateRoom_Room_Is_Not_Unique_ThrownException() {
        String roomName = "Test room with a same name";
        String countryCode = "US";
        RoomForm newRoomForm = new RoomForm(roomName, countryCode);
        Answer<Boolean> returnTrue = new Answer<Boolean>(){
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        };
        doAnswer(returnTrue).when(roomRepository).isContainRoom(anyString());
        service.createRoom(newRoomForm);
        verify(roomRepository, times(0)).createRoom(any(Room.class));
        verify(lampRepository, times(0)).createLamp(any(Lamp.class));
        verify(roomRepository, times(0)).updateRoom(any(Room.class));
    }

    @Test
    public void testUpdateRoom_Room_Changes_Country_Code() {
        String roomName = "Test room with a same name";
        String countryCode = "US";
        Integer id = 51;
        RoomForm form = new RoomForm(roomName, countryCode);
        Answer<Boolean> returnFalse = new Answer<Boolean>(){
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return false;
            }
        };
        Lamp lamp = new Lamp(id, true);
        Room returnedRoom =  new Room(roomName, countryCode,lamp);
        returnedRoom.setId(id);
        when(roomRepository.getRoomById(id)).thenReturn(Optional.of(returnedRoom));
        doAnswer(returnFalse).when(roomRepository).isContainRoom(anyString(), anyString());
        service.updateRoom(id, form);
        verify(roomRepository, times(1)).updateRoom(any(Room.class));
    }

    @Test
    public void testUpdateRoom_Room_Renames_On_New_Unique_Name() {
        String roomName = "Test room with a new unique name";
        String countryCode = "US";
        Integer id = 51;
        RoomForm form = new RoomForm(roomName, countryCode);
        Answer<Boolean> returnFalse = new Answer<Boolean>(){
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return false;
            }
        };
        Lamp lamp = new Lamp(id, true);
        String oldName = "Test room with a old name";
        Room returnedRoom =  new Room(oldName, countryCode,lamp);
        returnedRoom.setId(id);
        when(roomRepository.getRoomById(id)).thenReturn(Optional.of(returnedRoom));
        doAnswer(returnFalse).when(roomRepository).isContainRoom(anyString(), anyString());
        service.updateRoom(id, form);
        verify(roomRepository, times(1)).updateRoom(any(Room.class));
    }

    @Test(expected = CreatingException.class)
    public void testUpdateRoom_Room_Renames_On_Not_Unique_Name_ThrownException() {
        String roomName = "Test room with a new unique name";
        String countryCode = "US";
        Integer id = 51;
        RoomForm form = new RoomForm(roomName, countryCode);
        Answer<Boolean> returnTrue = new Answer<Boolean>(){
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        };
        Lamp lamp = new Lamp(id, true);
        String oldName = "Test room with a old name";
        Room returnedRoom =  new Room(oldName, countryCode,lamp);
        returnedRoom.setId(id);
        doAnswer(returnTrue).when(roomRepository).isContainRoom(anyString(), anyString());
        when(roomRepository.getRoomById(id)).thenReturn(Optional.of(returnedRoom));
        service.updateRoom(id, form);
        verify(roomRepository, times(0)).updateRoom(any(Room.class));
    }

    @Test(expected = NonexistentEntityException.class)
    public void testUpdateRoom_Room_Not_Exists_ThrownException() {
        String roomName = "Test room ";
        String countryCode = "US";
        Integer id = 50;
        RoomForm form = new RoomForm(roomName, countryCode);
        when(roomRepository.getRoomById(id)).thenReturn(Optional.empty());
        service.updateRoom(id, form);
        verify(roomRepository, times(0)).updateRoom(any(Room.class));
    }

    @Test
    public void testDeleteRoom_Existed_Room_With_Lamp() {
        String countryCode = "US";
        Integer id = 52;
        Lamp lamp = new Lamp(id, true);
        String name = "Test room with a old name";
        Room returnedRoom =  new Room(name, countryCode,lamp);
        returnedRoom.setId(id);
        when(roomRepository.getRoomById(id)).thenReturn(Optional.of(returnedRoom));
        service.deleteRoom(id);
        verify(lampRepository, times(1)).deleteLamp(any(Lamp.class));
        verify(roomRepository, times(1)).deleteRoom(any(Room.class));
    }

    @Test
    public void testDeleteRoom_Existed_Room_Without_Lamp() {
        String countryCode = "US";
        Integer id = 49;
        Lamp lamp = null;
        String name = "Test room with a old name";
        Room returnedRoom =  new Room(name, countryCode,lamp);
        returnedRoom.setId(id);
        when(roomRepository.getRoomById(id)).thenReturn(Optional.of(returnedRoom));
        service.deleteRoom(id);
        verify(lampRepository, times(0)).deleteLamp(any(Lamp.class));
        verify(roomRepository, times(1)).deleteRoom(any(Room.class));
    }

    @Test(expected = NonexistentEntityException.class)
    public void testDeleteRoom_Not_Existed_Room_ThrownException() {
        Integer id = 50;
        when(roomRepository.getRoomById(id)).thenReturn(Optional.empty());
        service.deleteRoom(id);
        verify(roomRepository, times(0)).deleteRoom(any(Room.class));
    }

}
