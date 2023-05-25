package com.mdtlabs.coreplatform.userservice.message;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.MessageValidator;
import com.mdtlabs.coreplatform.common.domain.Paged;
import com.mdtlabs.coreplatform.common.response.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <p>
 * The `SuccessResponse` class is a Java class that provides constructors for creating success responses with messages,
 * entities, and total counts.
 * </p>
 *
 * @param <T> - object
 * @author Vigneshkumar created on Jun 30, 2022
 */
public class SuccessResponse<T> extends ResponseEntity<Object> {

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, a `Paged` object
    // containing a list of entities of type `T` and a count, and an `HttpStatus` responseCode. It uses the `getMessage`
    // method of the `MessageValidator` class to retrieve a message based on the `SuccessCode` key and the
    // `Constants.SUCCESS` value. It then calls another constructor of the `SuccessResponse` class with the retrieved
    // message, a `null` entity object, the `entity` list obtained from the `Paged` object, `responseCode`, and the count
    // obtained from the `Paged` object. This constructor is used to create a success response with a message, a list of
    // entities, and a total count.
    public SuccessResponse(SuccessCode successCode, Paged<T> paged, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), paged, responseCode);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `String` message, a `Paged` object containing
    // a list of entities of type `T` and a count, and an `HttpStatus` responseCode. It calls another constructor of the
    // `SuccessResponse` class with the same `message`, a `null` entity object, the `entity` list obtained from the `Paged`
    // object, `responseCode`, and the count obtained from the `Paged` object. This constructor is used to create a success
    // response with a message, a list of entities, and a total count.
    public SuccessResponse(String message, Paged<T> paged, HttpStatus responseCode) {
        this(message, null, paged.getList(), responseCode, paged.getCount());
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, a `List` of entities, and
    // an `HttpStatus` responseCode. It uses the `getMessage` method of the `MessageValidator` class to retrieve a message
    // based on the `SuccessCode` key and the `Constants.SUCCESS` value. It then calls another constructor of the
    // `SuccessResponse` class with the retrieved message, a `null` entity object, the `entity` list, the `responseCode`,
    // and a `null` totalCount. This constructor is used to create a success response with a message, a list of entities,
    // and no total count.
    public SuccessResponse(SuccessCode successCode, List<T> entity, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, entity, responseCode, null);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, a `List` of entities, a
    // `long` totalCount, and an `HttpStatus` responseCode. It uses the `getMessage` method of the `MessageValidator` class
    // to retrieve a message based on the `SuccessCode` key and the `Constants.SUCCESS` value. It then calls another
    // constructor of the `SuccessResponse` class with the retrieved message, a `null` entity object, the `entity` list,
    // the `responseCode`, and the `totalCount`. This constructor is used to create a success response with a message, a
    // list of entities, and a total count.
    public SuccessResponse(SuccessCode successCode, List<T> entity, long totalCount, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, entity, responseCode, totalCount);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, an `Object` entity, a
    // `long` totalCount, and an `HttpStatus` responseCode. It uses the `getMessage` method of the `MessageValidator` class
    // to retrieve a message based on the `SuccessCode` key and the `Constants.SUCCESS` value. It then calls another
    // constructor of the `SuccessResponse` class with the retrieved message, the `entity` object, a `null` list of
    // entities, the `responseCode`, and the `totalCount`. This constructor is used to create a success response with a
    // message, an entity object, and a total count.
    public SuccessResponse(SuccessCode successCode, Object entity, long totalCount, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), entity, null, responseCode, totalCount);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `String` message, a `List` of entities, and an
    // `HttpStatus` responseCode. It calls another constructor of the `SuccessResponse` class with the same `message`, a
    // `null` entity object, the `entity` list, `responseCode`, and a `null` totalCount. This constructor is used to create
    // a success response with a message and a list of entities.
    public SuccessResponse(String message, List<T> entity, HttpStatus responseCode) {
        this(message, null, entity, responseCode, null);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, an `Object` entity, and an
    // `HttpStatus` responseCode. It uses the `getMessage` method of the `MessageValidator` class to retrieve a message
    // based on the `SuccessCode` key and the `Constants.SUCCESS` value. It then calls another constructor of the
    // `SuccessResponse` class with the retrieved message, the `entity` object, a `null` list of entities, the
    // `responseCode`, and a `null` totalCount. This constructor is used to create a success response with a message and an
    // entity object.
    public SuccessResponse(SuccessCode successCode, Object entity, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), entity, null, responseCode, null);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `String` message, an `Object` entity, and an
    // `HttpStatus` responseCode. It calls another constructor of the `SuccessResponse` class with the same `message`,
    // `entity`, `null` list of entities, `responseCode`, and `null` totalCount. This constructor is used to create a
    // success response with a message and an entity object.
    public SuccessResponse(String message, Object entity, HttpStatus responseCode) {
        this(message, entity, null, responseCode, null);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum and an `HttpStatus`
    // responseCode. It uses the `getMessage` method of the `MessageValidator` class to retrieve a message based on the
    // `SuccessCode` key and the `Constants.SUCCESS` value. It then creates a new `SuccessMessage` object with the
    // retrieved message, `null` entity object, `null` list of entities, the integer value of the `responseCode`, and a
    // `null` totalCount. Finally, it calls the superclass constructor of `ResponseEntity` with the `SuccessMessage` object
    // and the `responseCode`. This constructor is used to create a success response with only a message and a response
    // code.
    public SuccessResponse(SuccessCode successCode, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, null, responseCode, null);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `String` message, an `Object` entity, a `List`
    // of entities, an `HttpStatus` responseCode, and a `Long` totalCount. It creates a new `SuccessMessage` object with a
    // `Boolean` value of `TRUE`, the `message`, the `entity`, the `entityList`, the integer value of the `responseCode`,
    // and the `totalCount`. Finally, it calls the superclass constructor of `ResponseEntity` with the `SuccessMessage`
    // object and the `responseCode`. This constructor is used to create a success response with a message, an entity
    // object, a list of entities, and a total count.
    public SuccessResponse(String message, Object entity, List<T> entityList, HttpStatus httpStatus,
                           Long totalCount) {
        super(new SuccessMessage<T>(Boolean.TRUE, message, entity, entityList,
                httpStatus.value(), totalCount), httpStatus);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, a `List` of entities, an
    // `HttpStatus` responseCode, and a `Long` totalCount. It uses the `getMessage` method of the `MessageValidator` class
    // to retrieve a message based on the `SuccessCode` key and the `Constants.SUCCESS` value. It then creates a new
    // `SuccessMessage` object with the retrieved message, a `null` entity object, the `entity` list, the integer value of
    // the `responseCode`, and the `totalCount`. Finally, it calls the superclass constructor of `ResponseEntity` with the
    // `SuccessMessage` object and the `responseCode`. This constructor is used to create a success response with a list of
    // entities and a total count.
    public SuccessResponse(SuccessCode successCode, List<T> entity, HttpStatus responseCode, Long totalCount) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, entity, responseCode, totalCount);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, an `Object` entity, an
    // `HttpStatus` responseCode, and a variable number of `String` arguments. It uses the `getMessage` method of the
    // `MessageValidator` class to retrieve a message based on the `SuccessCode` key and the `Constants.SUCCESS` value, and
    // any additional arguments passed in. It then creates a new `SuccessMessage` object with the retrieved message, the
    // `entity` object, a `null` list of entities, the integer value of the `responseCode`, and a `null` totalCount.
    // Finally, it calls the superclass constructor of `ResponseEntity` with the `SuccessMessage` object and the
    // `responseCode`.
    public SuccessResponse(SuccessCode successCode, Object entity, HttpStatus responseCode, String... args) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()), Constants.SUCCESS, args),
                entity, null, responseCode, null);
    }

    // This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum, an `Object` entity, a
    // `long` totalCount, an `HttpStatus` responseCode, and a variable number of `String` arguments.
    public SuccessResponse(SuccessCode successCode, Object entity, long totalCount, HttpStatus responseCode,
                           String... args) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()), Constants.SUCCESS, args),
                entity, null, responseCode, totalCount);
    }
}
