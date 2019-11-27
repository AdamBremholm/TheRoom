package iths.theroom.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RequestException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
