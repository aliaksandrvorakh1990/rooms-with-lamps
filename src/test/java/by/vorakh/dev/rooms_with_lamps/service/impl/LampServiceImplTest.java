package by.vorakh.dev.rooms_with_lamps.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import by.vorakh.dev.rooms_with_lamps.exception.NonexistentEntityException;
import by.vorakh.dev.rooms_with_lamps.exception.RoomAccessException;
import by.vorakh.dev.rooms_with_lamps.model.LampCondition;
import by.vorakh.dev.rooms_with_lamps.repository.LampRepository;
import by.vorakh.dev.rooms_with_lamps.repository.RoomRepository;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class LampServiceImplTest {
    
    @Mock
    private LampRepository lampRepository;
    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private LampServiceImpl service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLampIntegerString_Lamp_Exists() {
        Optional<LampCondition> extected = Optional.of(new LampCondition(true));
        Integer id = 1;
        String userCountryCode = "BY";
        Optional<Lamp> returnedLamp = Optional.of(new Lamp(id, true));
        when(lampRepository.getLamp(id, userCountryCode)).thenReturn(returnedLamp);
        Optional<LampCondition> actual = service.getLamp(id, userCountryCode);
        assertEquals(extected,actual);
    }

    @Test(expected = RoomAccessException.class)
    public void testGetLampIntegerString_Lamp_Not_Exists_Thrown_Exception() {
        Optional<LampCondition> extected = Optional.empty();
        Integer id = 3;
        String userCountryCode = "US";
        Optional<Lamp> returnedLamp = Optional.empty();
        when(lampRepository.getLamp(id, userCountryCode)).thenReturn(returnedLamp);
        Optional<LampCondition> actual = service.getLamp(id, userCountryCode);
        assertEquals(extected,actual);
    }

    @Test
    public void testPress_Lamp_Exists() {
        Integer id = 1;
        String userCountryCode = "BY";
        Optional<Lamp> returnedLamp = Optional.of(new Lamp(id, true));
        when(lampRepository.getLamp(id, userCountryCode)).thenReturn(returnedLamp);
        service.press(id, userCountryCode);
        verify(lampRepository, times(1)).updateLamp(any(Lamp.class));
    }
    
    @Test(expected = RoomAccessException.class)
    public void testPress_Lamp_Not_Exists_Thrown_Exception() {
        Integer id = 1;
        String userCountryCode = "BY";
        Optional<Lamp> returnedLamp = Optional.empty();
        when(lampRepository.getLamp(id, userCountryCode)).thenReturn(returnedLamp);
        service.press(id, userCountryCode);
        verify(lampRepository, times(0)).updateLamp(any(Lamp.class));
    }


    @Test
    public void testUpdateLamp_Lamp_Exists() {
        Integer id = 1;
        LampCondition editedLampForm = new LampCondition(false);
        Optional<Lamp> returnedLamp = Optional.of(new Lamp(id, true));
        when(lampRepository.getLamp(id)).thenReturn(returnedLamp);
        service.updateLamp(id, editedLampForm);
        verify(lampRepository, times(1)).updateLamp(any(Lamp.class));
    }

    @Test(expected = NonexistentEntityException.class)
    public void testUpdateLamp_Not_Exists_Thrown_Exception() {
        Integer id = 4;
        LampCondition editedLampForm = new LampCondition(false);
        Optional<Lamp> returnedLamp = Optional.empty();
        when(lampRepository.getLamp(id)).thenReturn(returnedLamp);
        service.updateLamp(id, editedLampForm);
        verify(lampRepository, times(0)).updateLamp(any(Lamp.class));
    }

    @Test
    public void testDeleteLamp_Lamp_Exists_And_No_Contains_Room() {
        Integer id = 1;
        Optional<Lamp> returnedLamp = Optional.of(new Lamp(id, true));
        Optional<Room> returnedRoom = Optional.empty();
        when(lampRepository.getLamp(id)).thenReturn(returnedLamp);
        when(roomRepository.getRoom(new Lamp(id, true))).thenReturn(returnedRoom);
        service.deleteLamp(id);
        verify(lampRepository, times(1)).deleteLamp(any(Lamp.class));
        verify(roomRepository, times(0)).updateRoom(any(Room.class));
    }

    @Test
    public void testDeleteLamp_Lamp_Exists_And_Contains_Room() {
        Integer id = 3;
        Lamp lamp = new Lamp(id, true);
        Room room = new Room();
        room.setId(2);
        room.setCountryCode("UK");
        room.setName("UK Test Room");
        room.setLamp(lamp);
        Optional<Lamp> returnedLamp = Optional.of(lamp);
        Optional<Room> returnedRoom = Optional.of(room);
        when(lampRepository.getLamp(id)).thenReturn(returnedLamp);
        when(roomRepository.getRoom(new Lamp(id, true))).thenReturn(returnedRoom);
        service.deleteLamp(id);
        verify(lampRepository, times(1)).deleteLamp(any(Lamp.class));
        verify(roomRepository, times(1)).updateRoom(any(Room.class));
    }
    @Test(expected = NonexistentEntityException.class)
    public void testDeleteLamp_Not_Exists_Thrown_Exception() {
        Integer id = 6;
        Optional<Lamp> returnedLamp = Optional.empty();
        when(lampRepository.getLamp(id)).thenReturn(returnedLamp);
        service.deleteLamp(id);
        verify(lampRepository, times(1)).deleteLamp(any(Lamp.class));
    }

}
