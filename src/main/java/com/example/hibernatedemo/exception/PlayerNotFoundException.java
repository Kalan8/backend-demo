package com.example.hibernatedemo.exception;

/**
 * Exception thrown when a Player entity is not found in the database.
 */
public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(Long id) {
        super("Player with id " + id + " not found");
    }

    public PlayerNotFoundException(String message) {
        super(message);
    }
}
