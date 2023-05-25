package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * <p>
 * The class `BadRequestException` extends `ServicesException` and provides constructors for creating
 * exceptions with different parameters.
 * </p>
 */
public class BadRequestException extends ServicesException {

    /**
     * <p>
     *Java constructor for BadRequestException with no arguments, calls constructor with default values.
     * </p>
     */
    public BadRequestException() {
        this(1001, BadRequestException.class.getSimpleName());
    }

    /**
     * <p>
     * Java constructor for BadRequestException with an Integer code argument, calls constructor with default message.
     * </p>
     * 
     * @param code code param is given
     */
    public BadRequestException(final Integer code) {
        this(code, BadRequestException.class.getSimpleName());
    }

    /**
     
     * 
     */

    /**
     * <p>
     * Java constructor for BadRequestException with an Integer code and String list parameters, calls parent constructor.
     * </p> 
     * 
     * @param code
     * @param params
     */
    public BadRequestException(final Integer code, final String... params) {
        super(code, params);
    }

    /**
     * <p>
     * Java constructor for BadRequestException that calls parent and sets code and message with parameters,
     *  for creating custom exceptions.
     * </p>
     * 
     * @param code the code param is given
     * @param message the message param is given
     */
    public BadRequestException(final Integer code, final String message) {
        super(code, message);
    }

    /**
     * <p>
     * Java BadRequestException constructor calls parent, sets code and message using code and params parameters.
     * </p>
     * 
     * @param code the code param is given 
     * @param params the params of List<string> is given
     */
    public BadRequestException(final Integer code, final List<String> params) {
        super(code, params);
    }
}
