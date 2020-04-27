package by.vorakh.dev.rooms_with_lamps.service;

import by.vorakh.dev.rooms_with_lamps.model.response.RoomWithLampID;
import by.vorakh.dev.rooms_with_lamps.model.form.RoomForm;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomResponse;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<Room> getAllRooms();

    List<RoomResponse> getAllRoomNames();

    Optional<Room> getRoom(Integer id);

    Optional<RoomWithLampID> getRoom(Integer id, String userCountryCode);

    Optional<Integer> createRoom(RoomForm newRoomForm);

    void updateRoom(Integer id, RoomForm editedRoomForm);

    void deleteRoom(Integer id);

}
