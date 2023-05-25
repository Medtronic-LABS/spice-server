package com.mdtlabs.coreplatform.common.exception;

import java.util.List;

/**
 * This class is used to validate all the validation code.
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
public class Validation extends ServicesException {

    // This is a constructor method for the `Validation` class that creates a `Validation` exception
    // with a default code value of 1001 and a default message that includes the simple name of the
    // `Validation` class. It calls another constructor of the same class `Validation` that takes in
    // two parameters - `code` and `Validation.class.getSimpleName()`. The second parameter is a string
    // that represents the simple name of the `Validation` class. By calling this constructor with
    // default values, it ensures that a `Validation` exception is always created with a code and
    // message, even if the user does not provide any parameters.
    public Validation() {
        this(1001, Validation.class.getSimpleName());
    }

    // This is a constructor method for the `Validation` class that takes in an integer `code` as a
    // parameter. It calls another constructor of the same class `Validation` that takes in two
    // parameters - `code` and `Validation.class.getSimpleName()`. The second parameter is a string
    // that represents the simple name of the `Validation` class. This constructor is used to create a
    // `Validation` exception with a specific `code` and a default message that includes the simple
    // name of the `Validation` class. If the `code` parameter is not provided, it will use the default
    // code value of 1001.
    public Validation(final Integer code) {
        this(code, Validation.class.getSimpleName());
    }

    // This is a constructor method for the `Validation` class that takes in an integer code and a
    // variable number of string parameters. It calls the constructor of the parent class
    // `ServicesException` and passes the code and params to it. This constructor is used to create a
    // `Validation` exception with a specific code and a variable number of parameters that can be used
    // to provide additional information about the exception. The use of the ellipsis (`...`) in the
    // parameter list indicates that the method can accept any number of string parameters.
    public Validation(final Integer code, final String... params) {
        super(code, params);
    }

   // This is a constructor method for the `Validation` class that takes in an integer code and a
   // string message as parameters. It calls the constructor of the parent class `ServicesException`
   // and passes the code and message to it. This constructor is used to create a `Validation`
   // exception with a specific code and message that can be used to provide additional information
   // about the exception.
    public Validation(final Integer code, final String message) {
        super(code, message);
    }

    // This is a constructor method for the `Validation` class that takes in an integer code and a list
    // of strings as parameters. It calls the constructor of the parent class `ServicesException` and
    // passes the code and params to it. This constructor is used to create a `Validation` exception
    // with a specific code and a list of parameters that can be used to provide additional information
    // about the exception.
    public Validation(final Integer code, final List<String> params) {
        super(code, params);
    }
}
