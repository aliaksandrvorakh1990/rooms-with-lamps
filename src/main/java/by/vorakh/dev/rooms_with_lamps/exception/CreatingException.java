package by.vorakh.dev.rooms_with_lamps.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(code = NOT_FOUND)
public class CreatingException extends RuntimeException {

    private static final long serialVersionUID = -509369591378203098L;

    public CreatingException() {}

    public CreatingException(String message) {
        super(message);
    }

    public CreatingException(String message, Throwable cause) {
        super(message, cause);
    }

}
