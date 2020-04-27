package by.vorakh.dev.rooms_with_lamps.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = FORBIDDEN)
public class RoomAccessException extends RuntimeException {

    private static final long serialVersionUID = 432205043945913098L;

    public RoomAccessException() {}

    public RoomAccessException(String message) {
        super(message);
    }

    public RoomAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
