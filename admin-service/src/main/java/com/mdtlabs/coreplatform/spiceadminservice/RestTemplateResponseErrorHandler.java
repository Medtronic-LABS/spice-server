package com.mdtlabs.coreplatform.spiceadminservice;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;

/**
 * <p>
 * The RestTemplateResponseErrorHandler class is used to handle HTTP response errors and throw corresponding exceptions
 * based on the status code.
 * </p>
 */
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    /**
     * <p>
     * This method is used to check if the HTTP response status code indicates an error.
     * </p>
     *
     * @param clientHttpResponse {@link ClientHttpResponse} The information such as the response status code,
     *                           headers, and body is given
     * @return A boolean value indicating whether the HTTP response from the client contains an error or not is returned
     */
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().isError();
    }

    /**
     * <p>
     * This method is used to handle different HTTP status codes and throws corresponding exceptions.
     * </p>
     *
     * @param clientHttpResponse {@link ClientHttpResponse} The information such as the response status code,
     *                           headers, and body is given
     */
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        HttpStatus statusCode;

        statusCode = clientHttpResponse.getStatusCode();

        switch (statusCode) {
            case CONFLICT -> throw new DataConflictException(1006);
            case NOT_FOUND -> throw new DataNotFoundException(1006);
            default -> throw new BadRequestException(1006);
        }
    }
}
