package by.vorakh.dev.rooms_with_lamps.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import org.springframework.stereotype.Repository;

import by.vorakh.dev.rooms_with_lamps.repository.RoomRepository;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Room> getAllRooms() {
        String selectAllRooms = "select r from Room r";
        return entityManager.createQuery(selectAllRooms, Room.class).getResultList();
    }

    @Override
    public boolean isContainRoom(String roomName) {
        String nameParameter = "n";
        String selectRoomsByName = "SELECT r FROM Room r WHERE r.name = :n";
        List<Room> roomsWithThisName =  entityManager.createQuery(selectRoomsByName, Room.class)
                .setParameter(nameParameter, roomName).getResultList();
        boolean isContain = !roomsWithThisName.isEmpty();
        return isContain;
    }

    @Override
    public boolean isContainRoom(String newRoomName, String oldRoomName) {
        String newNameParameter = "n";
        String oldnameParameter = "o";
        String selectRoomsByName = "SELECT r FROM Room r WHERE r.name = :n AND r.name != :o ";
        List<Room> roomsWithThisName =  entityManager.createQuery(selectRoomsByName, Room.class)
                .setParameter(newNameParameter, newRoomName)
                .setParameter(oldnameParameter, oldRoomName)
                .getResultList();
        boolean isContain = !roomsWithThisName.isEmpty();
        return isContain;
    }

    @Override
    public Optional<Room> getRoomById(Integer id) {
        return Optional.ofNullable(entityManager.find(Room.class, id));
    }

    @Override
    public Optional<Room> getRoom(Integer id, String countryCode) {
        String codeParameter = "c";
        String idParameter = "i";
        String selectRoomsByIdAndCountry = "SELECT r FROM Room r WHERE r.countryCode = :c AND r.id = :i";
        List<Room> roomsWithThisName =  entityManager.createQuery(selectRoomsByIdAndCountry, Room.class)
                .setParameter(codeParameter, countryCode).setParameter(idParameter, id)
                .getResultList();
        Optional<Room> room = (!roomsWithThisName.isEmpty())
                ? Optional.of(roomsWithThisName.get(0))
                : Optional.empty();
        return room;
    }

    @Override
    public Optional<Room> getRoom(Lamp roomLamp) {
        String lampParameter = "l";
        String selectRoomsByLamp = "SELECT r FROM Room r WHERE r.lamp = :l";
        List<Room> roomsWithThisName =  entityManager.createQuery(selectRoomsByLamp, Room.class)
                .setParameter(lampParameter, roomLamp)
                .getResultList();
        Optional<Room> room = (!roomsWithThisName.isEmpty())
                ? Optional.of(roomsWithThisName.get(0))
                : Optional.empty();
        return room;
    }

    @Override
    public Optional<Integer> createRoom(Room newRoom) {
        entityManager.persist(newRoom);
        entityManager.flush();
        Integer id = newRoom.getId();
        return Optional.of(id);
    }

    @Override
    public void updateRoom(Room editedRoom) {
        entityManager.merge(editedRoom);
    }

    @Override
    public void deleteRoom(Room deletedRoom) {
        entityManager.remove(deletedRoom);
    }

}
