package iths.theroom.exception;

import org.springframework.http.HttpStatus;

public abstract class RequestException extends Throwable {

    private final String message;
    private final HttpStatus httpStatus;

    RequestException(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

    public HttpStatus getHttpStatus(){
        return this.httpStatus;
    }

    public String getResponseBody(){
        return "{\n" +
                "\"status\":\"" + httpStatus.value() + "\",\n" +
                "\"reason\":\"" + httpStatus.getReasonPhrase() + "\",\n" +
                "\"details\":\"" + message + "\"\n" +
                "}";
    }
}
