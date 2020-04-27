package by.vorakh.dev.rooms_with_lamps.service.impl;

import by.vorakh.dev.rooms_with_lamps.exception.NonexistentEntityException;
import by.vorakh.dev.rooms_with_lamps.exception.RoomAccessException;
import by.vorakh.dev.rooms_with_lamps.model.LampCondition;
import by.vorakh.dev.rooms_with_lamps.repository.LampRepository;
import by.vorakh.dev.rooms_with_lamps.repository.RoomRepository;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;
import by.vorakh.dev.rooms_with_lamps.service.LampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LampServiceImpl implements LampService {

    private LampRepository lampRepository;
    private RoomRepository roomRepository;

    @Autowired
    public LampServiceImpl(LampRepository lampRepository, RoomRepository roomRepository) {
        this.lampRepository = lampRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Lamp> getAllLamp() {
        return lampRepository.getAllLamps();
    }

    @Override
    public Optional<Lamp> getLamp(Integer id) {
        Optional<Lamp> foundLamp = lampRepository.getLamp(id);
        return foundLamp;
    }

    @Override
    public Optional<LampCondition> getLamp(Integer id, String userCountryCode) {
        Optional<Lamp> lampByIdAndCountry = lampRepository.getLamp(id, userCountryCode);
        lampByIdAndCountry.orElseThrow(RoomAccessException::new);
        Optional<LampCondition> foundLamp = lampByIdAndCountry.map(lamp -> {
            boolean isLightsOn = lamp.getIsLightsOn();
            return new LampCondition(isLightsOn);
        });
        return foundLamp;
    }

    @Override
    @Transactional
    public void press(Integer id, String userCountryCode) {
        Optional<Lamp> lampByIdAndCountry =lampRepository.getLamp(id, userCountryCode);
        Lamp lampById = lampByIdAndCountry.orElseThrow(RoomAccessException::new);
        boolean previousIsLightsOn = lampById.getIsLightsOn();
        boolean newIsLightsOn = (previousIsLightsOn) ? false : true;
        lampById.setLightsOn(newIsLightsOn);
        lampRepository.updateLamp(lampById);
    }

    @Override
    @Transactional
    public Optional<Integer> createLamp(LampCondition newLampForm) {
        boolean isLightsOn = newLampForm.getIsLightsOn();
        Lamp newLamp = new Lamp(isLightsOn);
        Optional<Integer> lampId = lampRepository.createLamp(newLamp);
        return lampId;
    }

    @Override
    @Transactional
    public void updateLamp(Integer id,LampCondition editedLampForm) {
        Optional<Lamp> lampById = lampRepository.getLamp(id);
        Lamp oldLamp = lampById.orElseThrow(NonexistentEntityException::new);
        boolean newIsLightsOn = editedLampForm.getIsLightsOn();
        oldLamp.setLightsOn(newIsLightsOn);
        lampRepository.updateLamp(oldLamp);
    }

    @Override
    @Transactional
    public void deleteLamp(Integer id) {
        Optional<Lamp> lampById = lampRepository.getLamp(id);
        Lamp deletedLamp = lampById.orElseThrow(NonexistentEntityException::new);
        Optional<Room> roomById = roomRepository.getRoom(deletedLamp);
        roomById.ifPresent(room -> {
            room.setLamp(null);
            roomRepository.updateRoom(room);
        });
        lampRepository.deleteLamp(deletedLamp);
    }

}
