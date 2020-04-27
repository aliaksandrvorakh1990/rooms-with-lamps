package by.vorakh.dev.rooms_with_lamps.repository.impl;

import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoomRepositoryImplIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RoomRepositoryImpl repository;
    private Room firstRoom;
    private Room secondRoom;
    private Lamp firstLamp;
    private Lamp secondLamp;
    private Lamp lampWithoutRoom;

    @Before
    public void setUp() throws Exception {
        firstLamp = new Lamp();
        firstLamp = entityManager.persistAndFlush(firstLamp);
        firstRoom = new Room("USA Room", "US",firstLamp);
        firstRoom = entityManager.persistAndFlush(firstRoom);
        secondLamp = new Lamp();
        secondLamp = entityManager.persistAndFlush(firstLamp);
        secondRoom = new Room("British Room", "UK",secondLamp);
        secondRoom = entityManager.persistAndFlush(secondRoom);
        lampWithoutRoom = new Lamp();
        lampWithoutRoom = entityManager.persistAndFlush(lampWithoutRoom);
    }

    @Test
    public void whenGetAllRooms_thenReturnListRoom() {
        List<Room> expected = Arrays.asList(firstRoom, secondRoom);
        List<Room> actual = repository.getAllRooms();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenIsContainRoomString_Existed_name_thenReturnTrue() {
        String existedName = firstRoom.getName();
        boolean actual = repository.isContainRoom(existedName);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenIsContainRoomString_NotExisted_name_thenReturnFalse() {
        String name = "NoT Existed Room";
        boolean actual = repository.isContainRoom(name);
        assertThat(actual).isFalse();
    }

    @Test
    public void whenIsContainRoomStringString_Existed_name_thenReturnTrue() {
        String oldName = firstRoom.getName();
        String newName = secondRoom.getName();
        boolean actual = repository.isContainRoom(newName, oldName);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenIsContainRoomStringString_NonExisted_name_thenReturnFalse() {
        String oldName = firstRoom.getName();
        String newName = "New Room";
        boolean actual = repository.isContainRoom(newName, oldName);
        assertThat(actual).isFalse();
    }

    @Test
    public void whenGetRoomById_ExistedRoom_thenReturnOptionalRoom() {
        Optional<Room> expected = Optional.of(secondRoom);
        Integer existingId = secondRoom.getId();
        Optional<Room> actual = repository.getRoomById(existingId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetRoomById_NonexistentRoom_thenReturnOptionalEmpty() {
        Optional<Room> expected = Optional.empty();
        Integer nonexistentId = 666;
        Optional<Room> actual = repository.getRoomById(nonexistentId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetRoomIntegerString_CorrectIdAndCountryCode_thenReturnOptionalRoom() {
        Optional<Room> expected = Optional.of(secondRoom);
        Integer id = secondRoom.getId();
        String countryCode = secondRoom.getCountryCode();
        Optional<Room> actual = repository.getRoom(id, countryCode);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetRoomIntegerString_IncorrectCountryCode_thenReturnOptionalEmpty() {
        Optional<Room> expected = Optional.empty();
        Integer id = secondRoom.getId();
        String countryCode = "AF";
        Optional<Room> actual = repository.getRoom(id, countryCode);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetRoomIntegerString_NonExistedID_thenReturnOptionalEmpty() {
        Optional<Room> expected = Optional.empty();
        Integer id = 11;
        String countryCode = "US";
        Optional<Room> actual = repository.getRoom(id, countryCode);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetRoomLamp_ExistedLampInRoom_thenReturnOptionalRoom() {
        Optional<Room> expected = Optional.of(secondRoom);
        Integer id = secondRoom.getId();
        String countryCode = secondRoom.getCountryCode();
        Optional<Room> actual = repository.getRoom(id, countryCode);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetRoomLamp_ExistedLampWithoutRoom_thenReturnOptionalEmpty() {
        Optional<Room> expected = Optional.empty();
        Optional<Room> actual = repository.getRoom(lampWithoutRoom);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenCreateRoom_thenReturnOptionalInteger() {
        Room newRoom = new Room("New Room", "BY");
        Optional<Integer> actual = repository.createRoom(newRoom);
        assertThat(actual).isNotEmpty();
    }

    @Test
    public void testUpdateRoom() {
        Integer id = firstRoom.getId();
        String newName = "German Room";
        String newCountryCode = "DE";
        Room expected = new Room(id, newName, newCountryCode, lampWithoutRoom);
        firstRoom.setName(newName);
        firstRoom.setCountryCode(newCountryCode);
        firstRoom.setLamp(lampWithoutRoom);
        repository.updateRoom(firstRoom);
        Room actual = entityManager.find(Room.class, id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testDeleteRoom() {
        Integer id = firstRoom.getId();
        repository.deleteRoom(firstRoom);
        Room actual = entityManager.find(Room.class, id);
        assertThat(actual).isNull();
    }

}
