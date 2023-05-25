package com.mdtlabs.coreplatform.common;

/**
 * <p>
 * To define the common static error parameter used all over the application.
 * </p>
 *
 * @author Niraimathi S created on Nov 16, 2022
 */
public final class ErrorConstants {
    public static final String COUNTRY_ID_NOT_NULL = "Country id must not be null";
    public static final String SITE_ID_NOT_NULL = "Site Id should not be null";
    public static final String PROGRAM_NAME_NOT_EMPTY = "Program name must not be empty";
    public static final String ACCOUNT_WORKFLOW_NAME_NOT_EMPTY = "Account workflow name should not be empty";
    public static final String ACCOUNT_WORKFLOW_VIEW_SCREEN_NOT_NULL = "View screens should not be empty or null";
    public static final String TYPE_NOT_NULL = "Type should not be empty";
    public static final String FORM_INPUT_NOT_NULL = "Form input should not be empty ";
    public static final String CATEGORY_NOT_NULL = "Category should not be empty";
    public static final String ACCOUNT_NAME_NOT_NULL = "Account name should not be empty";
    public static final String CLINICAL_WORKFLOW_NOT_NULL = "Clinical workflows should not be null ";
    public static final String AVG_SYSTOLIC_NOT_NULL = "Average systolic should not be empty";
    public static final String AVG_DIASTOLIC_NOT_NULL = "Average diastolic should not be empty";
    public static final String IS_REGULAR_SMOKER = "IsRegularSmoker should not be empty";
    public static final String SYMPTOM_NAME_NOT_NULL = "Symptom name should not be empty";
    public static final String SYMPTOM_ID_NOT_NULL = "Symptom id should not be null";
    public static final String COMPLIANCE_NAME_NOT_NULL = "Compliance name should not be empty";
    public static final String COMPLIANCE_ID_NOT_NULL = "Compliance Id should not be empty";
    public static final String BP_LOG_DETAILS_NOT_EMPTY = "BPLog details should not be empty";
    public static final String BP_LOG_DETAILS_MIN_SIZE = "BpLog details should contains minimum 2 values";
    public static final String FIRST_NAME_NOT_NULL = "Firstname should not be empty";
    public static final String LAST_NAME_NOT_NULL = "Lastname should not be empty";
    public static final String INITIAL_NOT_NULL = "Initial should not be empty";
    public static final String PHONE_NUMBER_NOT_NULL = "Phone number should not be empty";
    public static final String PHONE_NUMBER_CATEGORY_NOT_NULL = "Phone number category should not be empty";
    public static final String COUNTRY_NOT_NULL = "Country should not be empty";
    public static final String COUNTY_NOT_NULL = "County should not be empty";
    public static final String SUB_COUNTY_NOT_NULL = "Sub county should not be empty";
    public static final String AGE_NOT_NULL = "Age should not be null";
    public static final String AGE_MIN_VALUE = "Age should be greater than 0";
    public static final String GENDER_NOT_NULL = "Gender should not be null";
    public static final String PATIENT_TRACK_ID_NOT_NULL = "PatientTrackId should not be null";
    public static final String PATIENT_VISIT_ID_NOT_NULL = "PatientVisitId should not be null";
    public static final String IS_REVIEWED_NOT_NULL = "IsReviewed should not be null";
    public static final String LAB_TEST_ID_NOT_NULL = "LabtestId should not be null";
    public static final String PATIENT_LAB_TEST_RESULT_NAME_NOT_NULL = "PatientLabtest result name should not be empty";
    public static final String RESULT_VALUE_NOT_NULL = "Result value should not be null";
    public static final String UNIT_NOT_NULL = "Unit should not be empty";
    public static final String DISPLAY_ORDER_NOT_NULL = "Display order should be empty";
    public static final String DISPLAY_ORDER_MIN_VALUE = "Display order should be greater then 0";
    public static final String NATIONAL_ID_NOT_NULL = "National Id should not be empty";
    public static final String LONGITUDE_NOT_NULL = "Longitude should not be empty";
    public static final String LATITUDE_NOT_NULL = "Latitude should not be empty";
    public static final String IS_REFER_ASSESSMENT_NOT_NULL = "IsReferAssessment should not be null";
    public static final String MEDICATION_ID_NOT_NULL = "Medication Id should not be null";
    public static final String DOSAGE_FREQUENCY_NOT_NULL = "Dosage frequency should not be null";
    public static final String DOSAGE_UNIT_NOT_NULL = "Dosage unit should not be null";
    public static final String PRESCRIBED_DAYS = "Prescribed days should not be empty";
    public static final String MEDICATION_NAME_NOT_NULL = "Medication name should not be empty";
    public static final String DOSAGE_UNIT_VALUE_NOT_NULL = "Dosage unit value should not be empty";
    public static final String DOSAGE_UNIT_NAME_NOT_NULL = "Dosage unit name should not be empty";
    public static final String DOSAGE_FREQUENCY_NAME_NOT_NULL = "Dosage frequency name should not be empty";
    public static final String DOSAGE_FORM_NAME_NOT_NULL = "Dosage form name should not be empty";
    public static final String COUNTRY_NAME_NOT_NULL = "Country name should not be empty";
    public static final String COUNTRY_CODE_NOT_NULL = "Country code should not be empty";
    public static final String UNIT_MEASUREMENT_NOT_NULL = "Unit measurement should not be empty";
    public static final String COUNTY_NAME_NOT_NULL = "County name should not be empty";
    public static final String SUB_COUNTY_NAME_NOT_NULL = "Sub county name should not be null";
    public static final String CLASSIFICATION_NAME_NOT_NULL = "Classification name should not be empty";
    public static final String BRAND_NAME_NOT_NULL = "Classification name should not be empty";
    public static final String CLASSIFICATION_ID_NOT_NULL = "Classification Id should not be null";
    public static final String BRAND_ID_NOT_NULL = "Brand Id should not be null";
    public static final String DOSAGE_FORM_ID_NOT_NULL = "Dosage form Id should not be null";
    public static final String SAME_PASSWORD = "New password cannot be same as old password";
    public static final String ERROR_USERNAME_PASSWORD_BLANK = "No Username and / or Password Provided";
    public static final String ERROR_INVALID_USER = "Invalid credentials";
    public static final String ERROR_ACCOUNT_DISABLED = "Disabled Account";
    public static final String ERROR_INVALID_ATTEMPTS = "Account locked due to multiple invalid login attempts.";
    public static final String INFO_USER_NOT_EXIST = "Username does not exist : ";
    public static final String INFO_USER_PASSWORD_NOT_MATCH = "Password doesn't match for the user : ";
    public static final String EXCEPTION_TOKEN_UTILS = "Exception occurred while loading token utils";
    public static final String INVALID_USER_ERROR = "{ \"error\": \"Invalid User\"}";
    public static final String LOGIN_ERROR = "Login Error ";
    public static final String ERROR_JWE_TOKEN = "Error while creating jwe token ";
    public static final String TOKEN_EXPIRED = "Token expired";
    public static final String EXCEPTION_DURING_TOKEN_UTIL = "Exception occurred while loading token utils";
    public static final String RESOLVER_ERROR = "Error message construction using resolver Error Message";
    public static final String LINK_EXPIRED = "Link has expired.";
    public static final String EMAIL_NOT_NULL = "Username/email Id should not be empty";
    public static final String ERROR_USER_DOES_NOT_ROLE = "You don't have a permission. Please contact administrator";
    public static final String LAB_TEST_NAME_NOT_EMPTY = "Labtest name should not be empty.";
    public static final String PASSWORD_NOT_EXIST = "Password not set for the user";
    public static final String PARENT_ORG_ID_NOT_NULL = "Parent Organization Id should not be null";
    public static final String SITE_NAME_NOT_NULL = "Site should not be empty";
    public static final String ADDRESS_TYPE_NOT_NULL = "Address type should not be empty";
    public static final String ADDRESS_USE_NOT_NULL = "Address use should not be empty";
    public static final String ADDRESS_NOT_NULL = "Address should not be empty";
    public static final String SITE_NOT_NULL = "Site should not be empty";
    public static final String POSTAL_CODE_NOT_NULL = "Postal code should not be empty";
    public static final String SITE_TYPE_NOT_NULL = "Site type should not be empty";
    public static final String ACCOUNT_ID_NOT_NULL = "Account ID should not be empty";
    public static final String OPERATING_UNIT_NOT_NULL = "Operating unit should not be empty";
    public static final String OPERATING_UNIT_ID_NOT_NULL = "Operating unit ID should not be empty";
    public static final String CULTURE_NOT_NULL = "Culture should not be empty";
    public static final String USER_MIN_SIZE = "Site should have atleast one user";
    public static final String TIMEZONE_NOT_NULL = "Timezone should not be empty";
    public static final String REQUEST_NOT_EMPTY = "Request should not be empty.";
    public static final String CITY_NOT_NULL = "City should not be Empty";
    public static final String SITE_LEVEL_NOT_NULL = "Site Level should not be empty";
    public static final String SIGNATURE_REQUIRED = "Signature required to proceed";
    public static final String LAB_TEST_NOT_FOUND = "No LabTest found for this ID - ";
    public static final String OU_NOT_FOUND = "No Operating Unit found for this ID - ";
    public static final String TRANSFER_TO_NOT_NULL = "Patient transfer to should not be null";
    public static final String TRANSFER_SITE_NOT_NULL = "Patient transfer site should not be null";
    public static final String OLD_SITE_NOT_NULL = "Old site should not be null";
    public static final String TRANSFER_REASON_NOT_NULL = "Patient transfer reason should not be null";
    public static final String TRANSFER_STATUS_NOT_NULL = "Patient transfer status should not be null";
    public static final String TRANSFER_ID_NOT_NULL = "Patient transfer ID should not be null";

    private ErrorConstants() {
    }
}
