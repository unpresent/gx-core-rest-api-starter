package ru.gx.core.rest.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gx.core.rest.rest.template.ExceptionRestResponseWrapper;

import static lombok.AccessLevel.PROTECTED;

@RestControllerAdvice
public class AdviceController {
    @Getter(PROTECTED)
    @NotNull
    private final ObjectMapper objectMapper;

    public AdviceController(@NotNull final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @ExceptionHandler
    public ResponseEntity<ExceptionRestResponse> handleCustomException(Exception throwable) {
        ExceptionRestResponse exceptionRestResponse;
        if(throwable instanceof ExceptionRestResponseWrapper exceptionRestResponseWrapper) {
            exceptionRestResponse = exceptionRestResponseWrapper.getExceptionRestResponse();
        } else {
            exceptionRestResponse = new ExceptionRestResponse(throwable.getClass().getCanonicalName(), throwable.getMessage(), throwable.getCause());
        }

        return ResponseEntity.internalServerError().body(exceptionRestResponse);
    }
}
