package iths.theroom.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {


    public ConflictException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ConflictException(Throwable err) {
        super(err);
    }

    public ConflictException(String errorMessage) {
        super(errorMessage);
    }

    public ConflictException() {
        super();
    }

}
