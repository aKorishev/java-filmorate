package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFound(final NotFoundException e) {
        log.debug(e.getMessage(), e);

        return new ErrorResponse(
                "NotFoundException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerNotValid(final NotValidException e) {
        log.debug(e.getMessage(), e);

        return new ErrorResponse(
                "NotValidException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerNotValid(final org.springframework.web.bind.MethodArgumentNotValidException e) {
        log.debug(e.getMessage(), e);

        return new ErrorResponse(
                "NotValidException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerOther(final Exception e) {
        log.debug(e.getMessage(), e);

        return new ErrorResponse(
                e.getClass().getName(),
                e.getMessage());
    }
}
