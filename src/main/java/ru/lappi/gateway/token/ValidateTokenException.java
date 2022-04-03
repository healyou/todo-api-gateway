package ru.lappi.gateway.token;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;

/**
 * Ошибка валидации токена, при которой надо вернуть результат
 * запроса валидации токена
 *
 * @author Nikita Gorodilov
 */
public class ValidateTokenException extends NestedRuntimeException {
    private final HttpStatus status;
    private final String responseMessage;

    public ValidateTokenException(HttpStatus status, String responseMessage) {
        super("");
        this.status = status;
        this.responseMessage = responseMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
