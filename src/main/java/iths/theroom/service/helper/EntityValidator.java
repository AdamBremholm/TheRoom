package iths.theroom.service.helper;

import iths.theroom.exception.BadRequestException;

public interface EntityValidator<T> {

    void validate(T t) throws BadRequestException;
}
