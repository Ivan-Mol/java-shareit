package ru.practicum.shareit.exception.exceptions;

public class OwnerIsInvalidException extends RuntimeException {
    public OwnerIsInvalidException(String message) {
        super(message);
    }
}