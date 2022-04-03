package ru.lappi.gateway.configuration.exception;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.lappi.gateway.token.ValidateTokenException;

import java.util.Map;

/**
 * @author Nikita Gorodilov
 */
public class CustomErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {
    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     * @since 2.4.0
     */
    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));

        Throwable error = getError(request);
        if (error instanceof ValidateTokenException) {
            /* Выдаём результат валидации токена */
            ValidateTokenException validateTokenException = (ValidateTokenException) error;
            return ServerResponse.status(validateTokenException.getStatus())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(validateTokenException.getResponseMessage()));
        } else {
            errorAttributes.put("message", error.getMessage());
            return ServerResponse.status(getHttpStatus(errorAttributes))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(errorAttributes));
        }
    }
}
