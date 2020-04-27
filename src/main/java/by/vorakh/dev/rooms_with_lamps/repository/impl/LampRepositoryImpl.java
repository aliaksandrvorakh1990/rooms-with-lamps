package by.vorakh.dev.rooms_with_lamps.repository.impl;

import by.vorakh.dev.rooms_with_lamps.repository.LampRepository;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class LampRepositoryImpl implements LampRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Lamp> getAllLamps() {
        return entityManager.createQuery("SELECT l FROM Lamp l", Lamp.class).getResultList();
    }

    @Override
    public Optional<Lamp> getLamp(Integer id) {
        return Optional.ofNullable(entityManager.find(Lamp.class, id));
    }

    @Override
    public Optional<Lamp> getLamp(Integer id, String countryCode) {
        String codeParameter = "c";
        String idParameter = "i";
        String selectLampsByName = "SELECT l FROM Room r JOIN r.lamp l WHERE r.countryCode = :c "
                + "AND l.id = :i";
        List<Lamp> lamps =  entityManager.createQuery(selectLampsByName, Lamp.class)
                .setParameter(codeParameter, countryCode).setParameter(idParameter, id)
                .getResultList();
        Optional<Lamp> lamp = (!lamps.isEmpty())
                ? Optional.of(lamps.get(0))
                : Optional.empty();
        return lamp;
    }

    @Override
    public Optional<Integer> createLamp(Lamp newLamp) {
        entityManager.persist(newLamp);
        entityManager.flush();
        Integer id = newLamp.getId();
        return Optional.of(id);
    }

    @Override
    public void updateLamp(Lamp editedLamp) {
        entityManager.merge(editedLamp);
    }

    @Override
    public void deleteLamp(Lamp deletedLamp) {
        entityManager.remove(deletedLamp);
    }

}
