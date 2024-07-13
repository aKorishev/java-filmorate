package ru.yandex.practicum.filmorate.exceptions;

public class IdIsAlreadyInUseException extends RuntimeException {
    public IdIsAlreadyInUseException(String message) {
        super(message);
    }
}
