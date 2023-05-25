package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * This class is used to handle SendMailException.
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
public class SendMailException extends ServicesException {

    // This is a constructor for the SendMailException class that takes no arguments. It calls another
    // constructor of the same class with two arguments: an integer value of 1001 and the simple name
    // of the SendMailException class. This is a way to provide default values for the exception code
    // and message when no arguments are provided.
    public SendMailException() {
        this(1001, SendMailException.class.getSimpleName());
    }

    // This is a constructor for the SendMailException class that takes an integer argument called
    // "code". It calls another constructor of the same class with two arguments: the "code" value
    // passed as argument and the simple name of the SendMailException class. This is a way to provide
    // a default value for the exception message when only the code is provided.
    public SendMailException(final Integer code) {
        this(code, SendMailException.class.getSimpleName());
    }

    // This is a constructor for the SendMailException class that takes an integer argument called
    // "code" and a variable number of string arguments called "params". It calls the constructor of
    // the parent class ServicesException with the "code" and "params" arguments. This constructor
    // allows for passing a variable number of string arguments as parameters, which can be useful when
    // the number of parameters needed is not known in advance.
    public SendMailException(final Integer code, final String... params) {
        super(code, params);
    }

    // This is a constructor for the SendMailException class that takes two arguments: an integer
    // argument called "code" and a string argument called "message". It calls the constructor of the
    // parent class ServicesException with the "code" and "message" arguments. This constructor allows
    // for creating an instance of the SendMailException class with a specific error code and message
    // that can be used to provide additional information about the error.
    public SendMailException(final Integer code, final String message) {
        super(code, message);
    }

    // This is a constructor for the SendMailException class that takes an integer argument called
    // "code" and a List of string arguments called "params". It calls the constructor of the parent
    // class ServicesException with the "code" and "params" arguments. This constructor allows for
    // passing a List of string arguments as parameters, which can be useful when the number of
    // parameters needed is not known in advance. The constructor is used to create an instance of the
    // SendMailException class with a specific error code and a list of parameters that can be used to
    // provide additional information about the error.
    public SendMailException(final Integer code, final List<String> params) {
        super(code, params);
    }
}
