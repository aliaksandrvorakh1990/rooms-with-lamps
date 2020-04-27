package by.vorakh.dev.rooms_with_lamps.controller;

import by.vorakh.dev.rooms_with_lamps.exception.NonexistentEntityException;
import by.vorakh.dev.rooms_with_lamps.model.response.IdModel;
import by.vorakh.dev.rooms_with_lamps.model.LampCondition;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import by.vorakh.dev.rooms_with_lamps.service.LampService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Api
@RestController
public class LampController {

    private final static String ID_DESCRIPTION = "The lamp ID.";
    private final static String ID_NOTE = "ID has to be greater than zero.";
    private final static String ID_EXAMPLE = "2";
    private final static String SERVER_ERROR = "Internal server error.";
    private final static String NONEXISTENT_LAMP = "The lamp does not exist.";
    private final static String NO_ACCESS = "Forbidden for this country.";
    
    private LampService service;
    
    @Autowired
    public LampController(LampService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get a list of existing lamps from the database.",
            response = Lamp.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @GetMapping("/api/lamps")
    public List<Lamp> getLamps() {
        return service.getAllLamp();
    }

    @ApiOperation(value = "Get a lamp by id.", notes = ID_NOTE, response = Lamp.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = NONEXISTENT_LAMP),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @GetMapping("/api/lamps/{id}")
    public Lamp getLampById(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id) {
        Lamp lamp = service.getLamp(id).orElseThrow(NonexistentEntityException::new);
        return lamp;
    }


    @ApiOperation(value = "Create a lamp in the database.", response = IdModel.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "The lamp exists or invalid data."),
            @ApiResponse(code = 500,message = SERVER_ERROR),
    })
    @PostMapping("/api/lamps")
    public IdModel createLamp(
            @ApiParam(value = "A lamp form for creating in the database.", required = true)
            @RequestBody @Valid  @NotNull LampCondition newLamp) {
        Optional<Integer> id = service.createLamp(newLamp);
        IdModel idResponse = id.map(i -> new IdModel(i)).get();
        return idResponse;
    }

    @ApiOperation("Update an existing lamp in the database.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "The lamp does not exist or invalid data."),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @PutMapping("/api/lamps/{id}")
    public void updateLamp(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id,
            @ApiParam(value = "A lamp form for updating in the database.", required = true)
            @RequestBody @Valid  @NotNull LampCondition editedLamp){
        service.updateLamp(id,editedLamp);
    }

    @ApiOperation(value = "Delete an existing lamp by id from the database.", notes = ID_NOTE)
    @ApiResponses({
            @ApiResponse(code = 404, message = NONEXISTENT_LAMP),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @DeleteMapping("/api/lamps/{id}")
    public void deleteLamp(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id){
        service.deleteLamp(id);
    }

    @ApiOperation(value = "Get a lamp for user.", notes = ID_NOTE, response = LampCondition.class)
    @ApiResponses({
            @ApiResponse(code = 403, message = NO_ACCESS),
            @ApiResponse(code = 404, message = NONEXISTENT_LAMP),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @GetMapping("/api/lamps/in/{id}/from/{code}")
    public LampCondition getLampForUser(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id,
            @ApiParam(value = ID_DESCRIPTION, example = "BY", required = true)
            @PathVariable("code") @Valid @Positive @NotNull String userCountryCode) {
        LampCondition lamp = service.getLamp(id, userCountryCode).get();
        return lamp;
    }

    @ApiOperation(value = "Change a lamp condition when press a button.",  notes = ID_NOTE)
    @ApiResponses({
            @ApiResponse(code = 403, message = NO_ACCESS),
            @ApiResponse(code = 404, message = NONEXISTENT_LAMP),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @PutMapping("/api/lamps/in/{id}/from/{code}")
    public void pressButton(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id,
            @ApiParam(value = ID_DESCRIPTION, example = "BY", required = true)
            @PathVariable("code") @Valid @Positive @NotNull String userCountryCode) {
        service.press(id, userCountryCode);
    }

}
