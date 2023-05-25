package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * The DataConflictException class is a subclass of ServicesException that handles exceptions related
 * to data conflicts.
 */
public class DataConflictException extends ServicesException {
   /**
    * <p>
    * Constructor method for DataConflictException class with default error code and message.
    * </p>
    */
    public DataConflictException() {
        this(1001, DataConflictException.class.getSimpleName());
    }

   /**
    * <p>
    * Constructor method for DataConflictException class with specified error code and default message.
    * </p>
    * 
    * @param code Integer value of error code
    * @param code
    */
    public DataConflictException(final Integer code) {
        this(code, DataConflictException.class.getSimpleName());
    }

   /** 
    * <p>
    * Constructor method for DataConflictException class with specified error code and variable message parameters.
    * </p>
    * 
    * @param code Integer value of error code
    * @param params List of error message parameters
    */
    public DataConflictException(final Integer code, final String... params) {
        super(code, params);
    }

    /**
     * <p>
     * Constructor method for DataConflictException class with specified error code and message.
     * </p>
     * 
     * @param code Integer value of error code
     * @param message Error message     
     */
    public DataConflictException(final Integer code, final String message) {
        super(code, message);
    }

    /**
     * <p>
     * Constructor method for DataConflictException class with specified error code and list of message parameters.
     * </p>
     * 
     * @param code Integer value of error code
     * @param params List of error message parameters 
     */
    public DataConflictException(final Integer code, final List<String> params) {
        super(code, params);
    }

}
