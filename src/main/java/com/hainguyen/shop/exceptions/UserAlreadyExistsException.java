package com.hainguyen.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException {

    // register user using mobiNumber.
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
