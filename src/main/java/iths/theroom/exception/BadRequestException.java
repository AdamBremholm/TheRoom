package iths.theroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RequestException {

    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }

}
