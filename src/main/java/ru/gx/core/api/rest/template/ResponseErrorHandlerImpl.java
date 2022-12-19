package ru.gx.core.api.rest.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.*;
import ru.gx.core.api.rest.ExceptionRestResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static lombok.AccessLevel.PROTECTED;

/**
 * Spring's default implementation of the ResponseErrorHandler interface.
 *
 * <p>This error handler checks for the status code on the ClientHttpResponse
 * Any code in the 4xx or 5xx series is considered to be an error.
 */
public class ResponseErrorHandlerImpl implements ResponseErrorHandler {
    @Getter(PROTECTED)
    @NotNull
    private final ObjectMapper objectMapper;

    public ResponseErrorHandlerImpl(@NotNull final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        return (statusCode != null ? hasError(statusCode) : hasError(rawStatusCode));
    }

    protected boolean hasError(HttpStatus statusCode) {
        return statusCode.isError();
    }

    protected boolean hasError(int unknownStatusCode) {
        HttpStatus.Series series = HttpStatus.Series.resolve(unknownStatusCode);
        return (series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR);
    }

    /**
     * Handle the error in the given response with the given resolved status code.
     * @throws UnknownHttpStatusCodeException in case of an unresolvable status code
     * @see #handleErrorInternal(ClientHttpResponse, HttpStatus)
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        if (statusCode == null) {
            byte[] body = getResponseBody(response);
            String message = getErrorMessage(response.getRawStatusCode(),
                    response.getStatusText(), body, getCharset(response));
            throw new UnknownHttpStatusCodeException(message,
                    response.getRawStatusCode(), response.getStatusText(),
                    response.getHeaders(), body, getCharset(response));
        }
        handleErrorInternal(response, statusCode);
    }

    /**
     * Return error message with details from the response body. For example:
     * <pre>
     * 404 Not Found: [{'id': 123, 'message': 'my message'}]
     * </pre>
     */
    private String getErrorMessage(
            int rawStatusCode, String statusText, @Nullable byte[] responseBody, @Nullable Charset charset) {

        String preface = rawStatusCode + " " + statusText + ": ";

        if (ObjectUtils.isEmpty(responseBody)) {
            return preface + "[no body]";
        }

        charset = (charset != null ? charset : StandardCharsets.UTF_8);

        String bodyText = new String(responseBody, charset);
        bodyText = LogFormatUtils.formatValue(bodyText, false);

        return preface + bodyText;
    }

    /**
     * Если в ответе - ExceptionRestResponse, прокидываем ExceptionRestResponseWrapper, иначе - формируем ошибку
     */
    protected void handleErrorInternal(ClientHttpResponse response, HttpStatus statusCode) throws ExceptionRestResponseWrapper {
        String statusText;
        try {
            statusText = response.getStatusText();
        } catch (IOException ioException) {
            statusText = statusCode.toString();
        }
        HttpHeaders headers = response.getHeaders();
        byte[] body = getResponseBody(response);

        Charset charset = getCharset(response);
        ExceptionRestResponse exceptionRestResponse = null;
        try {
            exceptionRestResponse = getErrorDetails(body, charset);
        } catch (ExceptionRestResponseTryDeserialize | IOException exceptionRestResponseTryDeserialize) {
            // do nothing
        }

        if (exceptionRestResponse != null
                && (exceptionRestResponse.getMessage() != null
                || exceptionRestResponse.getCause() != null
                || exceptionRestResponse.getClassName() != null)) {
            throw new ExceptionRestResponseWrapper(exceptionRestResponse);
        }

        String message = getErrorMessage(statusCode.value(), statusText, body, charset);

        switch (statusCode.series()) {
            case CLIENT_ERROR -> throw HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
            case SERVER_ERROR -> throw HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
            default -> throw new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
        }
    }

    private static class ExceptionRestResponseTryDeserialize extends Exception {
    }

    private ExceptionRestResponse getErrorDetails(@Nullable byte[] responseBody, @Nullable Charset charset) throws ExceptionRestResponseTryDeserialize, IOException {
        if (ObjectUtils.isEmpty(responseBody)) {
            throw new ExceptionRestResponseTryDeserialize();
        }

        return objectMapper.readValue(responseBody, ExceptionRestResponse.class);
    }

    /**
     * Read the body of the given response (for inclusion in a status exception).
     *
     * @param response the response to inspect
     * @return the response body as a byte array,
     * or an empty byte array if the body could not be read
     */
    protected byte[] getResponseBody(ClientHttpResponse response) {
        try {
            return FileCopyUtils.copyToByteArray(response.getBody());
        } catch (IOException ex) {
            // ignore
        }
        return new byte[0];
    }

    /**
     * Determine the charset of the response (for inclusion in a status exception).
     *
     * @param response the response to inspect
     * @return the associated charset, or {@code null} if none
     */
    @Nullable
    protected Charset getCharset(ClientHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        return (contentType != null ? contentType.getCharset() : null);
    }

}