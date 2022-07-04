package ru.gx.core.rest.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class ExceptionRestResponse {
    String className;
    String message;
    Throwable cause;

    @JsonCreator
    public ExceptionRestResponse(
            @JsonProperty("className") @NotNull final String className,
            @JsonProperty("message") @NotNull final String message,
            @JsonProperty("cause") @NotNull final Throwable cause
    ) {
        this.className = className;
        this.message = message;
        this.cause = cause;
    }
}
