package by.vorakh.dev.rooms_with_lamps.service.impl;

import by.vorakh.dev.rooms_with_lamps.exception.CreatingException;
import by.vorakh.dev.rooms_with_lamps.exception.NonexistentEntityException;
import by.vorakh.dev.rooms_with_lamps.exception.RoomAccessException;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomWithLampID;
import by.vorakh.dev.rooms_with_lamps.model.form.RoomForm;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomResponse;
import by.vorakh.dev.rooms_with_lamps.repository.LampRepository;
import by.vorakh.dev.rooms_with_lamps.repository.RoomRepository;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;
import by.vorakh.dev.rooms_with_lamps.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    private LampRepository lampRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, LampRepository lampRepository) {
        this.roomRepository = roomRepository;
        this.lampRepository = lampRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    @Override
    public List<RoomResponse> getAllRoomNames() {
        return roomRepository.getAllRooms().stream()
                .filter(room -> room.getLamp() != null)
                .map(room -> {
                    Integer id = room.getId();
                    String name = room.getName();
                    return new RoomResponse(id, name);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> getRoom(Integer id) {
        Optional<Room> foundRoom = roomRepository.getRoomById(id);
        return foundRoom;
    }

    @Override
    public Optional<RoomWithLampID> getRoom(Integer id, String userCountryCode) {
        Optional<Room> roomByIdAndCountry = roomRepository.getRoom(id, userCountryCode);
        roomByIdAndCountry.orElseThrow(RoomAccessException::new);
        Optional<RoomWithLampID> foundRoom = roomByIdAndCountry
                .map(room -> {
                    Integer roomId = room.getId();
                    String name = room.getName();
                    Integer lampId = Optional.ofNullable(room.getLamp()).map(lamp -> lamp.getId())
                            .orElseThrow(NonexistentEntityException::new);
                    return new RoomWithLampID(roomId, name, lampId);
                });
        return foundRoom;
    }



    @Override
    @Transactional
    public Optional<Integer> createRoom(RoomForm newRoomForm) {
        Room newRoom = Optional.of(newRoomForm)
                .filter(room -> !roomRepository.isContainRoom(room.getName()))
                .map(roomForm -> {
                    String name = roomForm.getName();
                    String codeCountry = roomForm.getCountryCode();
                    return new Room(name, codeCountry);
                })
                .orElseThrow(CreatingException::new);
        Integer id = roomRepository.createRoom(newRoom)
                .orElseThrow(CreatingException::new);
        Lamp newLamp = new Lamp();
        lampRepository.createLamp(newLamp);
        newRoom.setLamp(newLamp);
        roomRepository.updateRoom(newRoom);
        return Optional.of(id);
    }

    @Override
    @Transactional
    public void updateRoom(Integer id, RoomForm editedRoomForm) {
        Room oldRoom = roomRepository.getRoomById(id).orElseThrow(NonexistentEntityException::new);
        String oldRoomName = oldRoom.getName();
        String newNameForRoom = editedRoomForm.getName();
        boolean hasSameRoomName = !oldRoomName.equals(newNameForRoom)
                && roomRepository.isContainRoom(newNameForRoom, oldRoomName);
        if (hasSameRoomName) {
            throw new CreatingException();
        }
        String countryCode = editedRoomForm.getCountryCode();
        oldRoom.setName(newNameForRoom);
        oldRoom.setCountryCode(countryCode);
        roomRepository.updateRoom(oldRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Integer id) {
        Optional<Room> roomByID = roomRepository.getRoomById(id);
        Room deletedRoom = roomByID.orElseThrow(NonexistentEntityException::new);
        Optional<Lamp> deletedLamp = Optional.ofNullable(deletedRoom.getLamp());
        roomRepository.deleteRoom(deletedRoom);
        deletedLamp.ifPresent(lamp ->  lampRepository.deleteLamp(lamp));
    }

}
