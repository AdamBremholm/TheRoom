package iths.theroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchUserException extends RuntimeException {


    public NoSuchUserException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public NoSuchUserException(Throwable err) {
        super(err);
    }

    public NoSuchUserException(String errorMessage) {
        super(errorMessage);
    }

    public NoSuchUserException() {
        super();
    }
}
