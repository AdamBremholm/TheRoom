package iths.theroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchMessageException extends RuntimeException {


    public NoSuchMessageException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public NoSuchMessageException(Throwable err) {
        super(err);
    }

    public NoSuchMessageException(String errorMessage) {
        super(errorMessage);
    }

    public NoSuchMessageException() {
        super();
    }
}
