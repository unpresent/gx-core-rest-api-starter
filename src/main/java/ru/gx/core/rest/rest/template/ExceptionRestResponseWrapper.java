package ru.gx.core.rest.rest.template;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.rest.rest.ExceptionRestResponse;

@Getter
public class ExceptionRestResponseWrapper extends RuntimeException {
    private final ExceptionRestResponse exceptionRestResponse;
    public ExceptionRestResponseWrapper(@NotNull final ExceptionRestResponse exceptionRestResponse) {
        super(exceptionRestResponse.getMessage());
        this.exceptionRestResponse = exceptionRestResponse;
    }
}
