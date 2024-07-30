package com.kirangs.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException exception, ServerWebExchange webExchange) {

        return Mono.just(new ErrorResponse(LocalDateTime.now(), Arrays.asList(exception.getMessage()), webExchange.getRequest().getPath().toString()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleRequestBodyError(WebExchangeBindException exception, ServerWebExchange webExchange) {
        List<String> errorMessages = exception.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return Mono.just(new ErrorResponse(LocalDateTime.now(), errorMessages, webExchange.getRequest().getPath().toString()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleException(Exception exception, ServerWebExchange webExchange) {

        return Mono.just(new ErrorResponse(LocalDateTime.now(), Arrays.asList(exception.getMessage()), webExchange.getRequest().getPath().toString()));
    }
}
