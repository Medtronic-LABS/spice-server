package com.mdtlabs.coreplatform.common.exception;


import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.util.StringUtil;
import feign.FeignException;
import org.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class is used to handle TeleCounselorException.
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@ControllerAdvice
public class ServicesExceptionHandler extends ResponseEntityExceptionHandler {

    private ExceptionResolver resolver;

    /**
     * <p>
     * This function initializes an instance of the ExceptionResolverImpl class during the
     * post-construction phase.
     * </p>
     */
    @PostConstruct
    public final void initiateErrorCodeToResolver() {
        resolver = new ExceptionResolverImpl();
    }

    /**
     * <p>
     * This method handles the invalid path exception.
     * </p>
     * 
     * @param runtimeException - RuntimeException
     * @return Error message
     */
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(value = InvalidPathException.class)
    @ResponseBody
    public ErrorMessage runtimeExceptionHandler(RuntimeException runtimeException) {
        Logger.logError(StringUtil.constructString(runtimeException.getClass().getName(),
                ExceptionConstants.MESSAGE_GENERIC, getErrorStackString(runtimeException)));

        return resolver.resolveError(HttpStatus.INTERNAL_SERVER_ERROR, runtimeException.getMessage());
    }

    /**
     * <p>
     * Handles the invalid method Argument exception.
     * </p>
     * 
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return Error message
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Logger.logError(StringUtil.constructString(ex.getClass().getName(), ExceptionConstants.MESSAGE_GENERIC,
                getErrorStackString(ex)));
        List<String> rejectedValues = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return handleExceptionInternal(ex, resolver.resolveError(HttpStatus.BAD_REQUEST, rejectedValues.toString()),
                headers, status, request);
    }

    /**
     * <p>
     * This method is used to handle Data Conflict Exception.
     * </p>
     * 
     * @param exception DataConflictException
     * @return Error message
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = DataConflictException.class)
    @ResponseBody
    protected final ErrorMessage handleDataConflict(DataConflictException exception) {
        Logger.logError(StringUtil.constructString(exception.getClass().getName(), ExceptionConstants.MESSAGE_GENERIC,
                getErrorStackString(exception)));
        return resolver.resolveError(HttpStatus.CONFLICT, exception.getMessage());
    }

    /**
     * <p>
     * Handles data not acceptable exception.
     * </p>
     * 
     * @param exception - DataNotAcceptableException Exception
     * @return ErrorMessage
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = DataNotAcceptableException.class)
    @ResponseBody
    protected final ErrorMessage handleDataNotAcceptable(DataNotAcceptableException exception) {
        Logger.logError(StringUtil.constructString(exception.getClass().getName(),
                ExceptionConstants.MESSAGE_GENERIC, getErrorStackString(exception)));
        return resolver.resolveError(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
    }


    /**
     * <p>
     * This method handles BadRequest Exception.
     * </p>
     * 
     * @param exception - BadRequestException
     * @return ErrorMessage
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    protected final ErrorMessage handleBadRequest(BadRequestException exception) {

        Logger.logError(StringUtil.constructString(exception.getClass().getName(), ExceptionConstants.MESSAGE_GENERIC,
                getErrorStackString(exception)));
        return resolver.resolveError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * <p>
     * This method handles DataNotFoundException.
     * </p>
     * 
     * @param exception - DataNotFoundException
     * @return error message
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = DataNotFoundException.class)
    @ResponseBody
    protected final ErrorMessage handleDataNotFoundException(DataNotFoundException exception) {
        Logger.logError(StringUtil.constructString(exception.getClass().getName(), ExceptionConstants.MESSAGE_GENERIC,
                getErrorStackString(exception)));
        return resolver.resolveError(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * <p>
     * This method handles TransactionSystemException.
     * </p>
     * 
     * @param validationException - TransactionSystemException
     * @return ErrorMessage
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = TransactionSystemException.class)
    @ResponseBody
    public ErrorMessage handleTransactionSystemException(TransactionSystemException validationException) {
        Logger.logError(StringUtil.constructString(validationException.getClass().getName(),
                ExceptionConstants.MESSAGE_GENERIC, getErrorStackString(validationException)));
        return resolver.resolveError(HttpStatus.BAD_REQUEST, validationException.getMessage());
    }

    /**
     * <p>
     * This method is responsible for handling all InternalServerError occurs
     * throughout the whole application.
     * </p>
     * 
     * @param exception - Exception
     * @return Error Message
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorMessage exceptionHandler(Exception exception) {
        Logger.logError(StringUtil.constructString(exception.getClass().getName(), ExceptionConstants.MESSAGE_GENERIC,
                getErrorStackString(exception)));

        return resolver.resolveError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    /**
     * <p>
     * This method is used to get error stack for an exception.
     * </p>
     * 
     * @param error - Exception object
     * @return error Stack
     */
    private String getErrorStackString(Exception error) {
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    /**
     * <p>
     * This method is used to handle bad request
     * </p>
     * 
     * @param servicesException - services exception
     * @return ErrorMessage - error trace message
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ServicesException.class,
            SpiceValidation.class})
    @ResponseBody
    protected final ErrorMessage handleException(ServicesException servicesException) {
        Logger.logError(StringUtil.constructString(servicesException.getClass().getName(),
                ExceptionConstants.MESSAGE_GENERIC, getErrorStackString(servicesException)));

        return resolver.resolveError(HttpStatus.BAD_REQUEST, servicesException.getMessage());
    }

    /**
     * <p>
     * This method is used to handle internal server error
     * </p>
     * 
     * @param exception- passing exception
     * @return ErrorMessage - error trace message
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = Unauthorized.class)
    @ResponseBody
    public ErrorMessage authExceptionHandler(Exception exception) {
        Logger.logError(StringUtil.constructString(exception.getClass().getName(), ExceptionConstants.MESSAGE_GENERIC,
                getErrorStackString(exception)));

        return resolver.resolveError(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    /**
     * <p>
     * This Java function handles a FeignException of type NotFound by setting the HTTP response status
     * to NOT_FOUND and returning a map of the exception content.
     * 
     * @param exception The parameter "exception" is an object of type FeignException, which is thrown
     * when a Feign client encounters an HTTP 404 Not Found error while making a request to a remote
     * server. This exception contains information about the error, such as the HTTP status code and
     * the response body.
     * @param response The `response` parameter is an instance of the `HttpServletResponse` class,
     * which represents the response that will be sent back to the client after the request has been
     * processed. It is used in this method to set the HTTP status code of the response to the status
     * code of the `FeignException
     * @return A `Map<String, Object>` is being returned.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseBody
    public Map<String, Object> handleFeignNotFoundException(FeignException exception, HttpServletResponse response) {
        response.setStatus(exception.status());
        return new JSONObject(exception.contentUTF8()).toMap();
    }

    /**
     * <p>
     * This Java function handles Feign exceptions related to not being able to accept the requested
     * content type.
     * </p>
     * 
     * @param exception The parameter "exception" is an object of type "FeignException" which is thrown
     * when a request made using Feign client fails. It contains information about the error that
     * occurred during the request, such as the HTTP status code and the response body.
     * @param response The `response` parameter is an instance of the `HttpServletResponse` class,
     * which represents the response that will be sent back to the client after the request has been
     * processed. It is used in this method to set the status code of the response to the status code
     * of the `FeignException`
     * @return A `Map<String, Object>` is being returned.
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(FeignException.NotAcceptable.class)
    @ResponseBody
    public Map<String, Object> handleFeignNotAcceptableException(FeignException exception,
                                                                 HttpServletResponse response) {
        response.setStatus(exception.status());
        return new JSONObject(exception.contentUTF8()).toMap();
    }

    /**
     * <p>
     * This Java function handles a FeignException of type BadRequest by setting the HTTP response
     * status and returning a map of the exception content.
     * 
     * @param exception The parameter "exception" is an object of type "FeignException" which is thrown
     * when a request made using Feign client fails due to a 4xx or 5xx HTTP status code. It contains
     * information about the error response received from the server, such as the status code, response
     * @param response The `response` parameter is an instance of the `HttpServletResponse` class,
     * which represents the response that will be sent back to the client after the request has been
     * processed. It is used in this method to set the status of the response to the status code of the
     * `FeignException` that
     * @return A `Map<String, Object>` is being returned.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.BadRequest.class)
    @ResponseBody
    public Map<String, Object> handleFeignBadRequestException(FeignException exception, HttpServletResponse response) {
        response.setStatus(exception.status());
        return new JSONObject(exception.contentUTF8()).toMap();
    }

    /**
     * <p>
     * This Java function handles a FeignException of type Conflict by setting the HTTP response status
     * and returning a map of the exception content.
     * 
     * @param exception The parameter "exception" is an object of type FeignException, which is an
     * exception thrown by the Feign client when a request to a remote service fails. It contains
     * information about the HTTP response status code, headers, and body.
     * @param response The `response` parameter is an instance of the `HttpServletResponse` class,
     * which represents the response that will be sent back to the client after the request has been
     * processed. It is used in this method to set the status of the response to the status code of the
     * `FeignException` that
     * @return A `Map<String, Object>` is being returned.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FeignException.Conflict.class)
    @ResponseBody
    public Map<String, Object> handleFeignConflictException(FeignException exception, HttpServletResponse response) {
        response.setStatus(exception.status());
        return new JSONObject(exception.contentUTF8()).toMap();
    }

}
