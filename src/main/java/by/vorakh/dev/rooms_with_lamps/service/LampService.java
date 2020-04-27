package by.vorakh.dev.rooms_with_lamps.service;

import by.vorakh.dev.rooms_with_lamps.model.LampCondition;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;

import java.util.List;
import java.util.Optional;

public interface LampService {

    List<Lamp> getAllLamp();

    Optional<Lamp> getLamp(Integer id);

    Optional<LampCondition> getLamp(Integer id, String userCountryCode);

    void press(Integer id, String userCountryCode);

    Optional<Integer> createLamp(LampCondition newLampForm);

    void updateLamp(Integer id, LampCondition editedLampForm);

    void deleteLamp(Integer id);

}
