package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * This class is used to handle InvalidPathException.
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
public class InvalidPathException extends ServicesException {

    // This is a constructor for the `InvalidPathException` class that creates an instance of the
    // exception with a default error code of 1001 and a message that is set to the simple name of the
    // `InvalidPathException` class. This constructor is useful when the exception needs to be created
    // with a generic error code and message.
    public InvalidPathException() {
        this(1001, InvalidPathException.class.getSimpleName());
    }

    // This is a constructor for the `InvalidPathException` class that takes in an error code as a
    // parameter. It calls another constructor of the same class that takes in both an error code and a
    // message as parameters. In this case, the message is set to the simple name of the
    // `InvalidPathException` class. This constructor is useful when the exception needs to be created
    // with a specific error code but a generic message.
    public InvalidPathException(final Integer code) {
        this(code, InvalidPathException.class.getSimpleName());
    }

    // This is a constructor for the `InvalidPathException` class that takes in an error code and a
    // variable number of parameters as arguments. It calls the constructor of the parent class
    // `ServicesException` with the same parameters to set the error code and parameters for the
    // exception. The use of `...` before the `params` parameter indicates that it can take in any
    // number of string arguments. This constructor is useful when the exception needs to include
    // multiple parameters to provide more information about the error.
    public InvalidPathException(final Integer code, final String... params) {
        super(code, params);
    }

   // This is a constructor for the `InvalidPathException` class that takes in an error code and a
   // message as parameters. It calls the constructor of the parent class `ServicesException` with the
   // same parameters to set the error code and message for the exception. This constructor is useful
   // when the exception needs to include a specific message to provide more information about the
   // error.
    public InvalidPathException(final Integer code, final String message) {
        super(code, message);
    }

    // This constructor is creating an instance of the `InvalidPathException` class with an error code
    // and a list of parameters. It is calling the constructor of the parent class `ServicesException`
    // with the same parameters to set the error code and parameters for the exception. This
    // constructor is useful when the exception needs to include multiple parameters to provide more
    // information about the error.
    public InvalidPathException(final Integer code, final List<String> params) {
        super(code, params);
    }
}
