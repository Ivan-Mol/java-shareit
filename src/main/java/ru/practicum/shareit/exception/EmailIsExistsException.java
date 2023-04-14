package ru.practicum.shareit.exception;

public class EmailIsExistsException extends RuntimeException {
    public EmailIsExistsException(String message) {
        super(message);
    }
}
