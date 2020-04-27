package by.vorakh.dev.rooms_with_lamps.repository;

import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;

import java.util.List;
import java.util.Optional;

public interface LampRepository {

    List<Lamp> getAllLamps();

    Optional<Lamp> getLamp (Integer id);

    Optional<Lamp> getLamp(Integer id, String countryCode);

    Optional<Integer> createLamp(Lamp newLamp);

    void updateLamp(Lamp editedLamp);

    void deleteLamp(Lamp deletedLamp);

}
