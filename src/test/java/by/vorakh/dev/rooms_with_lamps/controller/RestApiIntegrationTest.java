package by.vorakh.dev.rooms_with_lamps.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import by.vorakh.dev.rooms_with_lamps.model.LampCondition;
import by.vorakh.dev.rooms_with_lamps.model.form.RoomForm;
import by.vorakh.dev.rooms_with_lamps.model.response.IdModel;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomResponse;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomWithLampID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void createRoom(){
        String url = "http://localhost:" + port + "/api/rooms";
        RoomForm form = new RoomForm("Belarus room", "BY");
        IdModel idModel = this.restTemplate.postForObject(url, form, IdModel.class);
        assertThat(idModel.getId() > 0);
    }

    @Test
    @Order(2)
    public void getAllRoomsNames_AfterCreatingFirstRoom() {
        RoomResponse[] expected = {new RoomResponse(1,"Belarus room")};
        String url = "http://localhost:" + port + "/api/rooms/in";
        RoomResponse[] actual = this.restTemplate.getForObject(url, RoomResponse[].class);
        assertThat(actual.length).isEqualTo(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(3)
    public void getRoomByIdAndCountry_EnterToFirst_Room(){
        RoomWithLampID expected = new RoomWithLampID(1,"Belarus room", 1);
        String url = "http://localhost:" + port + "/api/rooms/in/1/from/BY";
        RoomWithLampID actual = this.restTemplate.getForObject(url, RoomWithLampID.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(4)
    public void getLampForUser(){
        LampCondition expected = new LampCondition(false);
        String url = "http://localhost:" + port + "/api/lamps/in/1/from/BY";
        LampCondition actual = this.restTemplate.getForObject(url,LampCondition.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(5)
    public void pressButton() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        String url = "http://localhost:" + port + "/api/lamps/in/1/from/BY";
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.PUT, entity,
                String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Order(6)
    public void getLampForUserAfterPressButton(){
        LampCondition expected = new LampCondition(true);
        String url = "http://localhost:" + port + "/api/lamps/in/1/from/BY";
        LampCondition actual = this.restTemplate.getForObject(url, LampCondition.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(7)
    public void createSecondRoom(){
        String url = "http://localhost:" + port + "/api/rooms";
        RoomForm form = new RoomForm("German room", "DE");
        IdModel idModel = this.restTemplate.postForObject(url, form, IdModel.class);
        assertThat(idModel.getId() == 2);
    }

    @Test
    @Order(8)
    public void getAllRoomsNames_AfterCreatingSecondRoom() {
        RoomResponse firstRoom = new RoomResponse(1,"Belarus room");
        RoomResponse secondRoom = new RoomResponse(2,"German room");
        RoomResponse[] expected = { firstRoom, secondRoom };
        String url = "http://localhost:" + port + "/api/rooms/in";
        RoomResponse[] actual = this.restTemplate.getForObject(url, RoomResponse[].class);
        assertThat(actual.length).isEqualTo(2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(9)
    public void getRoomByIdAndCountry_EnterToSecond_Room_ThenReturn403Status() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        String url = "http://localhost:" + port + "/api/rooms/in/2/from/BY";
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity,
                String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    @Order(10)
    public void getLampForUser_ForForbindenUser_ThenReturn403Status() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        String url = "http://localhost:" + port + "/api/lamps/in/2/from/BY";
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity,
                String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    @Order(11)
    public void createRoom_Room_With_Existing_Name_ThenReturn404Status() {
        RoomForm form = new RoomForm("Belarus room", "BY");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RoomForm> entity = new HttpEntity<RoomForm>(form, headers);
        String url = "http://localhost:" + port + "/api/rooms";
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.POST, entity,
                String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        ;
    }

}