package ru.lappi.gateway.token.resttempate;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Все результаты
 *
 * @author Nikita Gorodilov
 */
public class NoErrorResponseHandler extends DefaultResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

    @Override
    protected boolean hasError(HttpStatus statusCode) {
        return false;
    }

    @Override
    protected boolean hasError(int unknownStatusCode) {
        return false;
    }
}
