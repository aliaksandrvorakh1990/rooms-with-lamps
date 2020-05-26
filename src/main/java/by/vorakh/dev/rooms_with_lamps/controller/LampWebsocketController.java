package by.vorakh.dev.rooms_with_lamps.controller;

import java.util.Optional;

import by.vorakh.dev.rooms_with_lamps.model.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import by.vorakh.dev.rooms_with_lamps.model.LampCondition;
import by.vorakh.dev.rooms_with_lamps.service.LampService;

@Controller
public class LampWebsocketController {

    private LampService service;

    @Autowired
    public LampWebsocketController(LampService service) {
        this.service = service;
    }

    @MessageMapping("/lamp/{lampId}")
    @SendTo("/topic/get-lamp/{lampId}")
    public LampCondition getLamp(@DestinationVariable("lampId") Integer lampId, CountryCode userCountryCode) {
        String code = userCountryCode.getCode();
        Optional<LampCondition> lamp = service.getLamp(lampId, code);
        return lamp.get();
    }

    @MessageMapping("/press/{lampId}")
    @SendTo("/topic/get-lamp/{lampId}")
    public LampCondition pressButton(@DestinationVariable("lampId") Integer lampId, CountryCode userCountryCode) {
        String code = userCountryCode.getCode();
        service.press(lampId, code);
        Optional<LampCondition> lamp = service.getLamp(lampId, code);
        return lamp.get();
    }

}
