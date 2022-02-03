package my.crud.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String error) {
        super(error);
    }
}