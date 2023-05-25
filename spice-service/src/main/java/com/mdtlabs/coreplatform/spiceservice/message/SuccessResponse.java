package com.mdtlabs.coreplatform.spiceservice.message;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.MessageValidator;
import com.mdtlabs.coreplatform.common.domain.Paged;
import com.mdtlabs.coreplatform.common.response.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <p>
 * Generic success response.
 * </p>
 *
 * @param <T> - object
 * @author Vigneshkumar created on Jun 30, 2022
 */

public class SuccessResponse<T> extends ResponseEntity<Object> {

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * a `Paged` object of type `T`, and an `HttpStatus` value.
     * </p>
     *
     * @param successCode  {@link SuccessCode} the succeesscode param is given
     * @param paged        {@link Paged<T>} the paged param is given
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     */
    public SuccessResponse(SuccessCode successCode, Paged<T> paged, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), paged, responseCode);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `String` message, a
     * `Paged` object of type `T`, and an `HttpStatus` value.
     * </p>
     *
     * @param message      {@link String} the message param is given
     * @param paged        {@link Paged<T>} the paged param is given
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     */
    public SuccessResponse(String message, Paged<T> paged, HttpStatus responseCode) {
        this(message, null, paged.getList(), responseCode, paged.getCount());
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * a `List` of entities of type `T`, and an `HttpStatus` value.
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param is given
     * @param entity       {@link List<T>} the entity param is passed
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     */
    public SuccessResponse(SuccessCode successCode, List<T> entity, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, entity, responseCode, null);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * a `List` of entities of type `T`, a `long` totalCount, and an `HttpStatus` value.
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param is given
     * @param entity       {@link List<T>} the entity param is passed
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     * @param totalCount   {@link Long} the totalcout is given
     */
    public SuccessResponse(SuccessCode successCode, List<T> entity, long totalCount, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, entity, responseCode, totalCount);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * an `Object` entity, a `long` totalCount, and an `HttpStatus` value.
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param is given
     * @param entity       {@link Object} the entity param is passed
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     * @param totalCount   {@link Long} the totalcout is given
     */
    public SuccessResponse(SuccessCode successCode, Object entity, long totalCount, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), entity, null, responseCode, totalCount);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `String` message, a `List`
     * of entities of type `T`, and an `HttpStatus` value.
     * </p>
     *
     * @param message      {@link String} the message param is given
     * @param entity       {@link List<T>} the entity param is passed
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     */
    public SuccessResponse(String message, List<T> entity, HttpStatus responseCode) {
        this(message, null, entity, responseCode, null);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * an `Object` entity, and an `HttpStatus` value.
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param is given
     * @param entity       {@link Object} the entity param is passed
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     */
    public SuccessResponse(SuccessCode successCode, Object entity, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), entity, null, responseCode, null);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `String` message, an
     * `Object` entity, and an `HttpStatus` value.
     * </p>
     *
     * @param message      {@link String} the message param is given
     * @param entity       {@link Object} the entity param is passed
     * @param responseCode {@link HttpStatus} the responsecode param is passed
     */
    public SuccessResponse(String message, Object entity, HttpStatus responseCode) {
        this(message, entity, null, responseCode, null);
    }

    /**
     * <p>
     * his is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value
     * and an `HttpStatus` value.
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param is given
     * @param responseCode {@link HttpStatus} the responseCode is given
     */
    public SuccessResponse(SuccessCode successCode, HttpStatus responseCode) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, null, responseCode, null);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `String` message, an
     * `Object` entity, a `List` of entities, an `HttpStatus` value, and a `Long` totalCount.
     * </p>
     *
     * @param message    {@link String} the message param is given
     * @param entity     {@link Object} the entity param is given
     * @param entityList {@link List<T>} the entityList param is given
     * @param httpStatus {@link HttpStatus} the httpStatus is given
     * @param totalCount {@link Long} the totalcout is given
     */
    public SuccessResponse(String message, Object entity, List<T> entityList, HttpStatus httpStatus,
                           Long totalCount) {
        super(new SuccessMessage<T>(Boolean.TRUE, message, entity, entityList,
                Integer.valueOf(httpStatus.value()), totalCount), httpStatus);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * an `Object` entity, a `long` totalCount, an `HttpStatus` value, and a variable number of
     * `String` arguments
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param
     * @param entity       {@link List<T>} the entity param
     * @param totalCount   {@link long} the totalcount param
     * @param responseCode {@link HttpStatus} the httpstatus response code
     */
    public SuccessResponse(SuccessCode successCode, List<T> entity, HttpStatus responseCode, Long totalCount) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()),
                Constants.SUCCESS), null, entity, responseCode, totalCount);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * an `Object` entity, a `long` totalCount, an `HttpStatus` value, and a variable number of
     * `String` arguments
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param
     * @param entity       {@link Object} the entity param
     * @param responseCode {@link HttpStatus} the httpstatus response code
     * @param args         {@link String} the string args param
     */
    public SuccessResponse(SuccessCode successCode, Object entity, HttpStatus responseCode, String... args) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()), Constants.SUCCESS, args),
                entity, null, responseCode, null);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * an `Object` entity, a `long` totalCount, an `HttpStatus` value, and a variable number of
     * </p>
     * `String` arguments
     *
     * @param successCode  {@link SuccessCode} the successcode param
     * @param entity       {@link Object} the entity param
     * @param totalCount   {@link long} the totalcount param
     * @param responseCode {@link HttpStatus} the httpstatus response code
     * @param args         {@link String} the string args param
     */
    public SuccessResponse(SuccessCode successCode, Object entity, long totalCount, HttpStatus responseCode,
                           String... args) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(successCode.getKey()), Constants.SUCCESS, args),
                entity, null, responseCode, totalCount);
    }

    /**
     * <p>
     * This is a constructor for the `SuccessResponse` class that takes in a `SuccessCode` enum value,
     * an `HttpStatus` value, and a variable number of `String` arguments.
     * </p>
     *
     * @param successCode  {@link SuccessCode} the successcode param
     * @param responseCode {@link HttpStatus} the httpstatus response code
     * @param args         {@link String} the string args param
     */
    public SuccessResponse(SuccessCode successCode, HttpStatus responseCode, String... args) {
        this(MessageValidator.getInstance().getMessage(Integer.toString(
                successCode.getKey()), Constants.SUCCESS, args), null, null, responseCode, null);
    }
}
