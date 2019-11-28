package iths.theroom.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RequestException {

    public ConflictException(String errorMessage) {
        super(errorMessage);
    }

}
