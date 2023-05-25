package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * <p>
 * This class is used to handle the DataNotFoundException.
 * </p>
 * 
 * @author Vigneshkumar Created on 30 Jun 2022
 */
public class DataNotFoundException extends ServicesException {

    /**
     * <p>
     * This method is used to generate generic exception
     * </p>
     */    
    public DataNotFoundException() {
        this(1001, DataNotFoundException.class.getSimpleName());
    }

    /**
     * <p>
     * Constructor method for the DataNotFoundException class.
     * Creates an instance of DataNotFoundException with a default message based on the provided code.
     * </p>
          * 
     * @param code Integer parameter representing the error code.
     */
    public DataNotFoundException(final Integer code) {
        this(code, DataNotFoundException.class.getSimpleName());
    }

    /**
     * <p>
     * Constructor method for the DataNotFoundException class.
     * Creates an instance of DataNotFoundException with a custom message based on the provided code and params.
     * </p>
          * 
     * @param code Integer parameter representing the error code.
     * @param params Variable number of String parameters providing additional information about the exception.
     */
    public DataNotFoundException(final Integer code, final String... params) {
        super(code, params);
    }

    /**
     * <p>
     * Constructor method for the DataNotFoundException class.
     * Creates an instance of DataNotFoundException with a custom message based on the provided code and message.
     * </p>
          * 
     * @param code Integer parameter representing the error code.
     * @param message String parameter providing a custom message for the exception.
     */
    public DataNotFoundException(final Integer code, final String message) {
        super(code, message);
    }

    /**
     * <p>
     * Constructor method for the DataNotFoundException class.
     * Creates an instance of DataNotFoundException with a custom message based on the provided code and params.
     * </p>
          * 
     * @param code Integer parameter representing the error code.
     * @param params List of String parameters providing additional information about the exception.
     */
    public DataNotFoundException(final Integer code, final List<String> params) {
        super(code, params);
    }
}
