package com.mdtlabs.coreplatform.spiceadminservice;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;

/**
 * <p>
 * This class has the test methods for RestTemplateResponseErrorHandler class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class RestTemplateResponseErrorHandlerTest {

    @InjectMocks
    RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    @Test
    void hasError() throws IOException {
        //given
        ClientHttpResponse clientHttpResponse = getClientHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);

        //then
        boolean hasError = restTemplateResponseErrorHandler.hasError(clientHttpResponse);
        assertTrue(hasError);
    }

    @Test
    void handleError() {
        //given
        ClientHttpResponse conflictResponse = getClientHttpResponse(HttpStatus.CONFLICT);
        ClientHttpResponse notFoundResponse = getClientHttpResponse(HttpStatus.NOT_FOUND);
        ClientHttpResponse badRequestResponse = getClientHttpResponse(HttpStatus.BAD_REQUEST);

        //then
        assertThrows(DataConflictException.class, () -> restTemplateResponseErrorHandler.handleError(conflictResponse));
        assertThrows(DataNotFoundException.class, () -> restTemplateResponseErrorHandler.handleError(notFoundResponse));
        assertThrows(BadRequestException.class, () -> restTemplateResponseErrorHandler.handleError(badRequestResponse));
    }

    private ClientHttpResponse getClientHttpResponse(HttpStatus httpsCode) {
        ClientHttpResponse clientHttpResponse = new ClientHttpResponse() {
            @Override
            public HttpHeaders getHeaders() {
                return null;
            }

            @Override
            public InputStream getBody() throws IOException {
                return null;
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return httpsCode;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 0;
            }

            @Override
            public String getStatusText() throws IOException {
                return null;
            }

            @Override
            public void close() {
            }
        };
        return clientHttpResponse;
    }
}