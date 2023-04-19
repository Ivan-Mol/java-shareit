package ru.practicum.shareit.exception;

public class OwnerIsInvalidException extends RuntimeException {
    public OwnerIsInvalidException(String message) {
        super(message);
    }
}