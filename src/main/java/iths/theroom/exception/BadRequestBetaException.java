package iths.theroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestBetaException extends RuntimeException {

    public BadRequestBetaException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public BadRequestBetaException(Throwable err) {
        super(err);
    }

    public BadRequestBetaException(String errorMessage) {
        super(errorMessage);
    }

    public BadRequestBetaException() {
        super();
    }
}
