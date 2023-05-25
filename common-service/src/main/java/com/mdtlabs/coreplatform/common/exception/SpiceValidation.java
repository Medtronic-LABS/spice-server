package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * The `SpiceValidation` class is a subclass of `ServicesException` that provides constructors for
 * creating instances of the class with specific error codes and additional information about the
 * error.
 */
public class SpiceValidation extends ServicesException {

    public SpiceValidation() {
        this(1001, SpiceValidation.class.getSimpleName());
    }

    // This is a constructor for the `SpiceValidation` class that takes an integer `code` as a
    // parameter. It calls another constructor of the same class `SpiceValidation` with two parameters:
    // `code` and `SpiceValidation.class.getSimpleName()`. The `SpiceValidation.class.getSimpleName()`
    // method returns the simple name of the `SpiceValidation` class, which is a string representation
    // of the class name without the package name. This constructor is used to create a new instance of
    // `SpiceValidation` with a specific error code and a default message that provides additional
    // information about the error.
    public SpiceValidation(final Integer code) {
        this(code, SpiceValidation.class.getSimpleName());
    }

    // This is a constructor for the `SpiceValidation` class that takes an integer code and a variable
    // number of string parameters. It calls the constructor of the parent class `ServicesException`
    // with the same parameters, passing them up the inheritance chain. This constructor is used to
    // create a new instance of `SpiceValidation` with a specific error code and a variable number of
    // parameters that can be used to provide additional information about the error. The use of the
    // ellipsis (`...`) in the parameter list indicates that the method can accept any number of string
    // arguments.
    public SpiceValidation(final Integer code, final String... params) {
        super(code, params);
    }

    // This is a constructor for the `SpiceValidation` class that takes an integer code and a string
    // message as parameters. It calls the constructor of the parent class `ServicesException` with the
    // same parameters, passing them up the inheritance chain. This constructor is used to create a new
    // instance of `SpiceValidation` with a specific error code and a message that provides additional
    // information about the error.
    public SpiceValidation(final Integer code, final String message) {
        super(code, message);
    }

    // This is a constructor for the `SpiceValidation` class that takes an integer code and a list of
    // strings as parameters. It calls the constructor of the parent class `ServicesException` with the
    // same parameters, passing them up the inheritance chain. This constructor is used to create a new
    // instance of `SpiceValidation` with a specific error code and a list of parameters that can be
    // used to provide additional information about the error.
    public SpiceValidation(final Integer code, final List<String> params) {
        super(code, params);
    }
}