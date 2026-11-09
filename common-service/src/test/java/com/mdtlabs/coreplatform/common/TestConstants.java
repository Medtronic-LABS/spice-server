package com.mdtlabs.coreplatform.common;

/**
 * <p>
 * The TestConstants class contains various constants used in testing, including integers, Longs, and Strings.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
public class TestConstants {

    public static final int TEN = 10;

    public static final Long ZERO = 0L;
    public static final Long ONE = 1L;
    public static final Long TWO = 2L;
    public static final Long FIVE = 5L;

    public static final String USER_NAME = "ghana_physicianprescriber@spice.mdt";
    public static final String PHONE_NUMBER = "987654321";
    public static final String EMPTY = "empty";
    public static final String STATUS_CODE = "20002";
    public static final String ARGUMENT = "argument";
    public static final String MESSAGE = "message";
    public static final String INVALID_TOKEN = "Invalid token.";
    public static final String JWT_TOKEN = TestKeys.JWT_TOKEN;
    public static final String REDIS_KEY = "spice:login:superuser@test.com:" + TestKeys.JWT_TOKEN;
}
