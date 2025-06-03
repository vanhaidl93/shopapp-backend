package com.hainguyen.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidParamException extends RuntimeException{
    public InvalidParamException(String message){
        super(message);
    }
}
