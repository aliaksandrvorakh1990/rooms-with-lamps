package by.vorakh.dev.rooms_with_lamps.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND)
public class NonexistentEntityException extends RuntimeException {

    private static final long serialVersionUID = 363782050995913098L;

    public NonexistentEntityException() {}

    public NonexistentEntityException(String message) {
        super(message);
    }

    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
