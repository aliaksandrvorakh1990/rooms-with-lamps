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
public class LampRepositoryImplIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private LampRepositoryImpl repository;

    private Lamp firstLamp;
    private Lamp secondLamp;
    private Room room;

    @Before
    public void setUp() throws Exception {
        firstLamp = new Lamp();
        secondLamp = new Lamp(true);
        firstLamp = entityManager.persistAndFlush(firstLamp);
        secondLamp = entityManager.persistAndFlush(secondLamp);
        room = new Room ("Some room", "US", secondLamp);
        room = entityManager.persistAndFlush(room);
    }

    @Test
    public void whenGetAllLamps_thenReturnListLamps(){
        List<Lamp> expected = Arrays.asList(firstLamp, secondLamp);
        List<Lamp> actual = repository.getAllLamps();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetLamp_thenReturnOptionalLamp(){
        Optional<Lamp> expected = Optional.of(secondLamp);
        Integer id  = secondLamp.getId();
        Optional<Lamp> actual = repository.getLamp(id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetLamp_ExistedLampInRoom_thenReturnOptionalLamp(){
        Optional<Lamp> expected = Optional.of(secondLamp);
        String countryCode = room.getCountryCode();
        Integer id = secondLamp.getId();
        Optional<Lamp> actual = repository.getLamp(id, countryCode);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenGetLamp_IncorrectCountryCode_thenReturnOptionalEmpty(){
        Optional<Lamp> expected = Optional.empty();
        String countryCode = "UK";
        Integer id = secondLamp.getId();
        Optional<Lamp> actual = repository.getLamp(id, countryCode);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenCreateLamp_thenReturnOptional() {
        Lamp lamp = new Lamp(true);
        Optional<Integer> actual = repository.createLamp(lamp);
        assertThat(actual).isNotEmpty();
    }

    @Test
    public void testUpdateLamp() {
        Integer id = firstLamp.getId();
        firstLamp.setLightsOn(true);
        repository.updateLamp(firstLamp);
        Lamp actual = entityManager.find(Lamp.class, id);
        assertThat(actual).isEqualTo(new Lamp(id, true));
    }

    @Test
    public void testDeleteLamp() {
        Integer id = firstLamp.getId();
        repository.deleteLamp(firstLamp);
        Lamp actual = entityManager.find(Lamp.class, id);
        assertThat(actual).isNull();
    }

}
