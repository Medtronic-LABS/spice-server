package com.mdtlabs.coreplatform.util;

import java.math.BigInteger;

/**
 * <p>
 * This class has the Constants for test classes.
 * </p>
 *
 * @author Divya S created on March 03, 2023
 */
public class TestConstants {

    public static final Long ONE = 1L;
    public static final String FORM_NAME = "input form";
    public static final String PASSWORD = "spice123";
    public static final String FIRST_NAME = "Physician";
    public static final String LAST_NAME = "Prescriber";
    public static final String PHONE_NUMBER = "987654321";
    public static final String USER_NAME = "superuser@test.com";
    public static final String COUNTRY_CODE = "+233";
    public static final String ROLE_NAME = "admin";
    public static final String LEVEL = "1";
    public static final String TOKEN = "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.ZmFrZQ.ZmFrZQ.ZmFrZQ.ZmFrZQ"; // gitleaks:allow fake fixture
    public static final BigInteger BIG_INTEGER = new BigInteger("23933818432444313743021423144977234903217280997" +
            "6941360700427350871784716546768330430525150773426239200080148463166977797698839442107053788235550472727" +
            "2206084182570429743757860110438359434017021272883147935826796830300214477736706139996967807814888545474" +
            "1138914411509844293364273693455823896754702683535600859263476999345765898496576612037987038355757375255" +
            "5297820174330954389126464958173886684923538152837850926534159092526276374283110920063868078040217455687" +
            "4529941065661163429897275687214721558862878695740178906728222182485085699701991767182598974383089766798" +
            "2400374652850206410222203345067240825712711422531427953");

    public static final String REDIS_KEY = "spice:login:superuser@test.com:" + TOKEN;
    public static final String ERROR_TOKEN = "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.ZmFrZQ.ZmFrZQ.ZmFrZQ.ZmFrZQ"; // gitleaks:allow fake fixture

}
