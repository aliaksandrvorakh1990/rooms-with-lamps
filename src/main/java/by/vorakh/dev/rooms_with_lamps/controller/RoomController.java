package by.vorakh.dev.rooms_with_lamps.controller;

import by.vorakh.dev.rooms_with_lamps.exception.NonexistentEntityException;
import by.vorakh.dev.rooms_with_lamps.model.form.RoomForm;
import by.vorakh.dev.rooms_with_lamps.model.response.IdModel;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomResponse;
import by.vorakh.dev.rooms_with_lamps.model.response.RoomWithLampID;
import by.vorakh.dev.rooms_with_lamps.repository.entity.Room;
import by.vorakh.dev.rooms_with_lamps.service.RoomService;
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
public class RoomController {

    private final static String ID_DESCRIPTION = "The room ID";
    private final static String ID_NOTE = "ID has to be greater than zero.";
    private final static String ID_EXAMPLE = "3";
    private final static String SERVER_ERROR = "Internal server error.";
    private final static String NONEXISTENT_ROOM = "The room does not exist.";
    private final static String NO_ACCESS = "Forbidden for this country.";
   
    private RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        super();
        this.service = service;
    }

    @ApiOperation(value = "Get a list of existing rooms from the database",
            response = Room.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @GetMapping("/api/rooms")
    public List<Room> getAllRooms(){
        List<Room> rooms = service.getAllRooms();
        return rooms;
    }

    @ApiOperation(value = "Get a room by id.", notes = ID_NOTE, response = Room.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = NONEXISTENT_ROOM),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @GetMapping("/api/rooms/{id}")
    public Room getRoomById(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id) {
        Room room = service.getRoom(id).orElseThrow(NonexistentEntityException::new);
        return room;
    }

    @ApiOperation(value = "Create a room in the database", response = IdModel.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "The lamp exists or invalid data."),
            @ApiResponse(code = 500,message = SERVER_ERROR),
    })
    @PostMapping("/api/rooms")
    public IdModel createRoom(
            @ApiParam(value = "A lamp form for creating in the database.", required = true)
            @RequestBody @Valid  @NotNull RoomForm newRoom) {
        Optional<Integer> id = service.createRoom(newRoom);
        IdModel idResponse = id.map(i -> new IdModel(i)).get();
        return idResponse;
    }

    @ApiOperation("Update an existing room in the database.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "The room does not exist or invalid data."),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @PutMapping("/api/rooms/{id}")
    public void updateRoom(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id,
            @ApiParam(value = "A lamp form for updating in the database.", required = true)
            @RequestBody @Valid  @NotNull RoomForm editedRoom){
        service.updateRoom(id, editedRoom);
    }

    @ApiOperation(value = "Delete an existing room by id from the database.",  notes = ID_NOTE)
    @ApiResponses({
            @ApiResponse(code = 404, message = NONEXISTENT_ROOM),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @DeleteMapping("/api/rooms/{id}")
    public void deleteRoom(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id){
        service.deleteRoom(id);
    }

    @ApiOperation(value = "Get a list of existing rooms from the database. Using for this app.",
            response = RoomResponse.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @GetMapping("/api/rooms/in")
    public List<RoomResponse> getAllRoomsNames(){
        List<RoomResponse> rooms = service.getAllRoomNames();
        return rooms;
    }

    @ApiOperation(value = "Get a room for user. . Using for this app.", notes = ID_NOTE, 
            response = RoomWithLampID.class)
    @ApiResponses({
            @ApiResponse(code = 403, message = NO_ACCESS),
            @ApiResponse(code = 404, message = NONEXISTENT_ROOM),
            @ApiResponse(code = 500, message = SERVER_ERROR)
    })
    @GetMapping("/api/rooms/in/{id}/from/{code}")
    public RoomWithLampID getRoomByIdAndCountry(
            @ApiParam(value = ID_DESCRIPTION, example = ID_EXAMPLE, required = true)
            @PathVariable("id") @Valid @Positive @NotNull Integer id,
            @ApiParam(value = ID_DESCRIPTION, example = "BY", required = true)
            @PathVariable("code") @Valid @Positive @NotNull String userCountryCode) {
        RoomWithLampID room = service.getRoom(id, userCountryCode).get();
        return room;
    }

}
