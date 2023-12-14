package com.ddbbackenddevchallenge.hitpoints.model.api.exceptions;

public class CreatureNotFoundException extends RuntimeException {

    public CreatureNotFoundException(final String name) {
        super("Could not find creature " + name);
    }
    
}
