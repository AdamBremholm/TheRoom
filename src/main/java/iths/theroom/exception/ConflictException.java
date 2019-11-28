package iths.theroom.exception;

import iths.theroom.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class ConflictException extends RequestException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException() {
        super(HttpStatus.CONFLICT);
    }

    public ConflictException(UserEntity userEntity) {
        super(HttpStatus.CONFLICT);
    }
}
