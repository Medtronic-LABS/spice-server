package com.mdtlabs.coreplatform.spiceadminservice.message;

/**
 * <p>
 * Success code to fetch message from property. Property
 * file(application.property) present in resource folder.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022
 */
public enum SuccessCode {

    ACCOUNT_UPDATE(2002),
    GET_ACCOUNT(2003),
    ACCOUNT_ACTIVATE(2004),
    ACCOUNT_DEACTIVATE(2005),
    GET_DEACTIVATED_ACCOUNT(2006),
    ACCOUNT_ADMIN_SAVE(2007),
    ACCOUNT_ADMIN_UPDATE(2008),
    ACCOUNT_ADMIN_DELETE(2009),
    GET_ACCOUNTS(2010),

    ACCOUNT_CUSTOMIZATION_SAVE(3001),
    ACCOUNT_CUSTOMIZATION_UPDATE(3002),
    GET_ACCOUNT_CUSTOMIZATION(3003),
    ACCOUNT_CUSTOMIZATION_DELETE(3004),

    ACCOUNT_WORKFLOW_SAVE(2101),
    ACCOUNT_WORKFLOW_UPDATE(2102),
    GET_ACCOUNT_WORKFLOW(2103),
    ACCOUNT_WORKFLOW_DELETE(2104),

    REGION_CUSTOMIZATION_SAVE(2151),
    REGION_CUSTOMIZATION_UPDATE(2152),
    GET_REGION_CUSTOMIZATION(2153),

    COUNTY_SAVE(11001),
    GET_COUNTY(11004),

    SUB_COUNTY_SAVE(21003),
    SUB_COUNTY_UPDATE(21004),
    GET_SUB_COUNTIES(21005),
    GET_SUB_COUNTY(21006),

    MEDICATION_SAVE(12001),
    MEDICATION_UPDATE(12002),
    GET_MEDICATION(12003),
    GET_MEDICATIONS(12004),
    MEDICATION_STATUS_UPDATE(12005),

    VALIDATE_MEDICATION(12011),

    LAB_TEST_SAVE(18001),
    LAB_TEST_UPDATE(18002),
    GET_LAB_TEST(18003),
    GET_LAB_TESTS(18004),
    LAB_TEST_DELETE(18005),
    LAB_TEST_VALIDATE(18006),

    COUNTRY_UPDATE(19002),
    GET_COUNTRY(19003),
    GET_COUNTRIES(19004),
    DEACTIVATE_COUNTRY(19009),


    PROGRAM_SAVE(14001),
    PROGRAM_DELETE(14002),
    GET_PROGRAM(14003),
    PROGRAM_STATUS_UPDATE(14005),

    SITE_UPDATE(27002),
    GET_SITE(27008),
    SITE_ADMIN_SAVE(27005),
    SITE_ADMIN_UPDATE(27006),
    SITE_ADMIN_REMOVE(27007),

    LAB_TEST_RESULT_RANGE_SAVE(28001),
    LAB_TEST_RESULT_RANGE_UPDATE(28002),
    LAB_TEST_RESULT_RANGE_DELETE(28003),
    GET_LAB_TEST_RESULT_RANGE(28004),

    REGION_ADMIN_SAVE(19011),
    REGION_ADMIN_UPDATE(19012),
    REGION_ADMIN_DELETE(19013),

    OU_UPDATE(29002),
    GOT_OU(29003),
    OU_ADMIN_SAVE(29004),
    OU_ADMIN_UPDATE(29005),
    OU_ADMIN_DELETE(29006),
    HEALTH_CHECK(30000);

    private final int key;

    // This is a constructor for the `SuccessCode` enum.
    // This allows each enum value to have a unique integer key associated with it.
    SuccessCode(int key) {
        this.key = key;
    }

    /**
     * <p>
     * The function returns the value of the "key" variable.
     * </p>
     *
     * @return An integer value which is the value of the instance variable `key` is returned
     */
    public int getKey() {
        return this.key;
    }
}