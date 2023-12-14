package com.ddbbackenddevchallenge.hitpoints.model.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HitPointAdvice {

    @ResponseBody
    @ExceptionHandler(CreatureNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String creatureNotFoundHandler(CreatureNotFoundException ex) {
        return ex.getMessage();
    }
    

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nullPointerExceptionHandler(NullPointerException ex) {
        return ex.getMessage();
    }
}
