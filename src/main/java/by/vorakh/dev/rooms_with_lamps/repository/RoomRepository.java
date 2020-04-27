package by.vorakh.dev.rooms_with_lamps.repository;

import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    List<Room> getAllRooms();

    boolean isContainRoom(String roomName);

    boolean isContainRoom(String newRoomName, String oldRoomName);

    Optional<Room> getRoomById(Integer id);

    Optional<Room> getRoom(Integer id,String countryCode);

    Optional<Room> getRoom(Lamp roomLamp);

    Optional<Integer> createRoom(Room newRoom);

    void updateRoom(Room editedRoom);

    void deleteRoom(Room deletedRoom);
}
