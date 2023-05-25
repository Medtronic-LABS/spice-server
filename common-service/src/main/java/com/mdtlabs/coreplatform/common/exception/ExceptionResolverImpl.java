package com.mdtlabs.coreplatform.common.exception;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.logger.Logger;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * <p>
 * This service class contain all the business logic for exception resolver
 * module and perform all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public class ExceptionResolverImpl implements ExceptionResolver {

    /**
     * {@inheritDoc}
     */
    public ErrorMessage resolveError(final HttpStatus statusCode, final String msg) {
        Logger.logInfo(ErrorConstants.RESOLVER_ERROR + msg);
        return ErrorMessage.builder()
                .dateTime(ZonedDateTime.now().toInstant().toEpochMilli())
                .errorCode(statusCode.value())
                .message(msg)
                .exception(msg)
                .status(Boolean.FALSE)
                .build();
    }
}
