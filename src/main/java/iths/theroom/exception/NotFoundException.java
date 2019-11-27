package iths.theroom.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RequestException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
