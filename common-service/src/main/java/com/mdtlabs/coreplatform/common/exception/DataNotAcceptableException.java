package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * <p>
 * The class `DataNotAcceptableException` is a subclass of `ServicesException` and provides
 * constructors for creating exceptions related to unacceptable data.
 * </p>
 * 
 */
public class DataNotAcceptableException extends ServicesException {
    
    /**
     * <p>
     * Constructs an instance of DataNotAcceptableException with default error code and message.
     * Calls another constructor with the same class, passing default error code and message as arguments.
     * </p>
     * 
     */
    public DataNotAcceptableException() {
        this(1001, DataNotAcceptableException.class.getSimpleName());
    }

    /**
     * <p>
     * Constructs an instance of DataNotAcceptableException with specified error code and default message.
     * Calls another constructor with the same class, passing specified error code and default message as arguments.
     * </p>
     * 
     * @param code the error code to be set for the exception. 
     */
    public DataNotAcceptableException(final Integer code) {
        this(code, DataNotAcceptableException.class.getSimpleName());
    }

    /**
     * <p>
     * Constructs an instance of DataNotAcceptableException with specified error code and variable number of message parameters.
     * Calls the constructor of the superclass ServicesException with specified error code and message parameters.
     * </p>
     * 
     * @param code the error code to be set for the exception.
     * @param params the variable number of message parameters to be set for the exception.
     */
    public DataNotAcceptableException(final Integer code, final String... params) {
        super(code, params);
    }

    /**
     * <p>
     * Constructs an instance of DataNotAcceptableException with specified error code and message.
     * Calls the constructor of the superclass ServicesException with specified error code and message.
     * </p>
     * 
     * @param code the error code to be set for the exception.
     * @param message the error message to be set for the exception.
     */
    public DataNotAcceptableException(final Integer code, final String message) {
        super(code, message);
    }

    /**
     * <p>
     * Constructs an instance of DataNotAcceptableException with specified error code and list of message parameters.
     * Calls the constructor of the superclass ServicesException with specified error code and message parameters.
     * </p>
     * 
     * @param code the error code to be set for the exception.
     * @param params the list of message parameters to be set for the exception.
     */
    public DataNotAcceptableException(final Integer code, final List<String> params) {
        super(code, params);
    }

}
