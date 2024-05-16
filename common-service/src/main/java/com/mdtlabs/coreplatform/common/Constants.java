package com.mdtlabs.coreplatform.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mdtlabs.coreplatform.common.model.dto.spice.CultureDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageFrequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.FrequencyType;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm;
import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;

/**
 * <p>
 * To define the common static parameter used all over the application.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public final class Constants {

    public static final String PACKAGE_CORE_PLATFORM = "com.mdtlabs.coreplatform";
    // Common Symbols & Strings
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String OPEN_BRACKET = "[";
    public static final String CLOSE_BRACKET = "]";
    public static final String HYPHEN = "-";
    public static final String ASTERISK_SYMBOL = "*";
    public static final String FORWARD_SLASH = "/";
    public static final String COMMA = ",";
    public static final String AND = "and";
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    public static final String COLON = ":";
    public static final String UNDER_SCORE = "_";
    // Date Format
    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssxxx";
    public static final String DATE_FORMAT = "dd MMM, yyyy";
    public static final String NO_DATA_FOUND = "No Data Found";
    public static final List<Object> NO_DATA_LIST = List.of();
    public static final String GMT = "GMT";
    public static final String RESET_NOTIFICATION_SUBJECT = "Spice Password Reset";
    public static final String APP_URL_EMAIL = "app_url_email";
    public static final String NEW_USER_CREATION = "User_Creation";
    public static final String FORGOT_PASSWORD_USER = "Forgot_Password";
    public static final String NOTIFICATION_STATUS_PROCESSED = "Notification status processed";
    public static final String NOTIFICATION_STATUS_FAILED = "Notification status failed";
    public static final String SAVING_EMAIL_NOTIFICATION_ERROR = "Error while saving notification for email";
    public static final String HEADER_CLIENT = "client";
    // AES_KEY
    public static final String AES_KEY_TOKEN = "te!e(0unsElor@321";
    // Application
    public static final String ISSUER = "SpiceApplication";
    public static final String ROLE_ID_PARAM = "roleId";
    public static final String PAGE_NUMBER = "pageNumber";
    public static final String TOKEN_ISSUER = "User";
    public static final String AUTH_TOKEN_SUBJECT = "Auth_Token";
    public static final Object WEB = "Web";
    public static final long THREE = 3;
    public static final String GET_CLASS = "getClass";
    public static final int NUMBER_THREE = 3;
    public static final long TWENTY_FOUR = 24;
    public static final String STRING_TWO = "2";
    public static final String STRING_THREE = "3";
    public static final String STRING_FOUR = "4";
    public static final String STRING_FIVE = "5";
    public static final String LOG_PREFIX_REQUEST = "|>";
    public static final String LOG_PREFIX_RESPONSE = "|<";
    public static final String SPLIT_CONTENT = "\r\n|\r|\n";
    public static final String BEARER = "Bearer ";
    public static final long AUTH_TOKEN_EXPIRY_MINUTES = 120;
    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final String MESSAGE = "message";
    public static final String INFO_USER_EXIST = "Login employee isEnabled : ";
    public static final String CONTENT_TEXT_TYPE = "text/x-json;charset=UTF-8";
    public static final String CACHE_HEADER_NAME = "Cache-Control";
    public static final String CACHE_HEADER_VALUE = "no-cache";
    public static final String RSA = "RSA";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_ID_PARAM = "userId";
    public static final String USER_DATA = "userData";
    public static final String APPLICATION_TYPE = "applicationType";
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String HIBERNATE_EJB_INTERCEPTOR = "hibernate.ejb.interceptor";
    public static final String HIBERNATE_SESSION_FACTORY = "hibernate.session_factory.interceptor";
    public static final String AUDIT = "Audit";
    public static final String GET = "get";
    public static final String DOCUMENTATION_SWAGGER = "documentation.swagger";
    public static final String DOCUMENTATION_SWAGGER_SERVICES = "documentation.swagger.services";
    public static final String TOKEN = "token";
    public static final String EMAIL = "email";
    public static final String LOGGER = "Logger";
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int FOUR = 4;
    public static final Boolean BOOLEAN_TRUE = Boolean.TRUE;
    public static final Boolean BOOLEAN_FALSE = Boolean.FALSE;
    public static final String TEXT = "text/*";
    public static final String APPLICATION_XML = "application/*+xml";
    public static final String APPLICATION_JSON = "application/*+json";
    public static final String AUTHORITY = "authority";
    public static final String DATE_TIME_FORMATTER = "d/M/yyyy";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final int WEB_NOTIFICATION_LIMIT = 5;
    public static final String NOTIFICATION_ID = "notificationId";
    public static final String TRUE = "true";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
    public static final String SMTP_SSL_PROTOCOL = "TLSv1.2";
    public static final String TEXT_HTML_CHARSET = "text/HTML; charset=UTF-8";
    public static final String FORMAT = "format";
    public static final String FLOWED = "flowed";
    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    public static final String ENCODING = "8bit";
    public static final String UTF_8 = "UTF-8";
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String AUTH_API = "Auth API";
    public static final String VERSION = "1.0";
    public static final String DOCUMENTATION_AUTH_API = "Documentation Auth API v1.0";
    public static final String DOCUMENTATION_USER_API = "Documentation User API v1.0";
    public static final String USER_API = "User API";
    public static final String API_ROLES_MAP_CLEARED = "api permission map cleared";
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String UPDATED_AT = "updatedAt";
    public static final String TOKENS = "tokens";
    public static final String IS_ACTIVE = "isActive";
    public static final String SEARCH_TERM_FIELD = "searchTerm";
    public static final String TENANT_IDS = "tenantIds";
    // Tenant
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TENANT_IDS_CLAIM = "tenantId";
    public static final String ROLE_ID_CLAIM = "roleId";
    public static final String ROLE_NAME_CLAIM = "roleName";
    public static final String HEADER_TENANT_ID = "TenantId";
    public static final String TENANT_FILTER_NAME = "tenantFilter";
    public static final String TENANT_PARAMETER_NAME = "tenantId";
    public static final String TENANT_PARAMETER_TYPE = "int";
    public static final String TENANT_COLUMN_NAME = "tenant_id";
    // Spring Components and warning
    // Date Format
    public static final String WEEK = "WEEK";
    public static final String DAY = "DAY";
    public static final String TIMEZONE_UTC = "UTC";
    public static final String NONE = "NONE";
    public static final String ENROLLED = "ENROLLED";
    public static final String SCREENED = "SCREENED";
    public static final String OBSERVATION = "OBSERVATION";
    public static final String HIGH = "High";
    public static final String LOW = "Low";
    public static final String MODERATE = "Moderate";
    public static final String HIGHER_MODERATE = "Higher Moderate";
    public static final String BOTH_MODERATE = "Both Moderate";
    public static final String BOTH_HIGHER_MODERATE = "Both Higher Moderate";
    public static final String GLUCOSE_MODERATE = "Glucose Moderate";
    public static final String SCREENING = "screening";
    public static final String ASSESSMENT = "assessment";
    public static final String ENROLLMENT = "enrollment";
    public static final String MEDICAL_REVIEW = "medicalreview";
    public static final String SYSTOLIC = "systolic";
    public static final String DIASTOLIC = "diastolic";
    public static final String RBS = "rbs";
    public static final String FBS = "fbs";
    public static final String DBM_GLUCOSE_MODERATE = "Take your prescribed medication and come back in 3 days for "
            + "a BP recheck. Your doctor may ask you to come for medical review";
    public static final String DBM_BOTH_MODERATE = "Take your prescribed medication and come back in 3 days for a BP "
            + "recheck. Your doctor may ask you to come for medical review.";
    public static final String DBM_HIGHER_MODERATE = "Take your prescribed medication and come back in 3 days for a "
            + "BP recheck. Your doctor may ask you to come for medical review";
    public static final String DBM_MODERATE = "Take your prescribed medication and come back in 3 days for a "
            + "BP recheck. Your doctor may ask you to come for medical review";
    public static final String DBM_HIGH = "Go to the hospital or clinic for further evaluation";
    public static final String DBM_LOW = "Great job! Continue to check blood pressure and take prescribed medications";
    public static final int BP_THRESHOLD_SYSTOLIC = 140;
    public static final int BP_THRESHOLD_DIASTOLIC = 90;
    public static final String BG_PROVISIONAL_FREQUENCY_NAME = "Physician Approval Pending";
    public static final String FREQUENCY_BP_CHECK = "BP Check";
    public static final String FREQUENCY_BG_CHECK = "BG Check";
    public static final String FREQUENCY_MEDICAL_REVIEW = "Medical Review";
    public static final String FREQUENCY_HBA1C_CHECK = "Hba1c Check";
    public static final String MEDICAL_REVIEW_FREQUENCY = "Medical Review Frequency";
    public static final String BP_CHECK_FREQUENCY = "Blood Pressure Check Frequency";
    public static final String BG_CHECK_FREQUENCY = "Blood Glucose Check Frequency";
    public static final String HBA1C_CHECK_FREQUENCY = "Hba1c Check Frequency";
    public static final String LABEL = "label";
    public static final String VALUE = "value";
    public static final String DIABETES_UNCONTROLLED_OR_POORLY_CONTROLLED = "Diabetes: Uncontrolled/Poorly controlled";
    public static final String DIABETES_WELL_CONTROLLED = "Diabetes: Well-controlled";
    public static final String PRE_DIABETES = "Pre-Diabetes";
    public static final String KNOWN_DIABETES_PATIENT = "known";
    public static final String GLUCOSE_UNIT_MG_DL = "mg/dL";
    public static final String GLUCOSE_UNIT_MMOL_L = "mmol/L";
    public static final String OTHER = "Other";
    public static final String UNIT = "unit";
    public static final String NEW = "NEW";
    public static final String MEDICAL_REVIEW_COMPLETED = "MEDICAL_REVIEW_COMPLETED";
    public static final String HYPERTENSION = "Hypertension";
    public static final String DIABETES = "Diabetes";
    public static final String DEFAULT = "default";
    public static final String MEDICATION_LIST = "medicationList";
    public static final String IS_PRESCRIPTION = "isPrescription";
    public static final String IS_INVESTIGATION = "isInvestigation";
    public static final String IS_MEDICAL_REVIEW = "isMedicalReview";
    public static final String MG_DL = "mg/dL";
    public static final String WORKFLOW_SCREENING = "Screening";
    public static final String WORKFLOW_ENROLLMENT = "Enrollment";
    public static final String WORKFLOW_ASSESSMENT = "Assessment";
    public static final String SIGNATURE_DATE_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String PHQ4 = "PHQ4";
    public static final String PHQ9 = "PHQ9";
    public static final String GAD7 = "GAD7";
    public static final Integer FBS_MMOL_L = 7;
    public static final Integer RBS_MMOL_L = 11;
    public static final Integer FBS_MG_DL = 126;
    public static final Integer RBS_MG_DL = 198;
    public static final String FREQ_KEY = "frequency_key";
    public static final Map<String, Map<String, String>> FREQUENCIES = Map.of("MEDICAL_REVIEW", Map.of("TYPE",
                    FREQUENCY_MEDICAL_REVIEW, LABEL.toUpperCase(), MEDICAL_REVIEW_FREQUENCY, FREQ_KEY.toUpperCase(),
                    "medicalReviewFrequency"), "BP_CHECK", Map.of("TYPE", FREQUENCY_BP_CHECK, LABEL.toUpperCase(),
                    BP_CHECK_FREQUENCY, FREQ_KEY.toUpperCase(), "bpCheckFrequency"), "BG_CHECK",
            Map.of("TYPE", FREQUENCY_BG_CHECK, LABEL.toUpperCase(), BG_CHECK_FREQUENCY, FREQ_KEY.toUpperCase(),
                    "bgCheckFrequency"), "HBA1C_CHECK", Map.of("TYPE", FREQUENCY_HBA1C_CHECK, LABEL.toUpperCase(),
                    HBA1C_CHECK_FREQUENCY, FREQ_KEY.toUpperCase(), "hba1cCheckFrequency"));
    public static final String MODULE = "Module";
    public static final String INPUT_FORM = "Input_form";
    public static final String CONSENT_FORM = "Consent_form";
    public static final String TYPE = "type";
    public static final String APP_TYPE = "appType";
    public static final String FORMS = "forms";
    public static final String OPTIONS = "options";
    public static final String QUESTIONS = "questions";
    public static final String TEMPLATE_TYPE_ENROLL_PATIENT = "ENROLL_PATIENT";
    public static final Object ENTITY = "entity";
    // Lifestyle type
    public static final String ACCOUNT = "account";
    public static final String REGION = "Country";
    public static final String OPERATING_UNIT = "operatingunit";
    public static final String COUNTRY = "country";
    public static final String SESSION_ALIVE = "Session alive";
    public static final String COUNT = "count";
    public static final String DATA = "data";
    public static final String EMAIL_FORM_USER = "User";
    public static final String CREATED_AT = "createdAt";
    public static final String SEARCH_TERM = "[^a-zA-Z0-9-()\\s]*";
    public static final String USER_SEARCH_TERM = "[^[^_.@%]a-zA-Z0-9-\\s]*";
    public static final String DIABETES_MELLITUS_TYPE_1 = "Diabetes Mellitus Type 1";
    public static final String DIABETES_MELLITUS_TYPE_2 = "Diabetes Mellitus Type 2";
    public static final String GESTATIONAL_DIABETES = "Gestational Diabetes(GDM)";
    public static final String HYPER_TENSION = "Hypertension";
    public static final String PRE_HYPER_TENSION = "Pre-Hypertension";
    public static final List<String> DIABETES_CONFIRM_DIAGNOSIS = List.of(PRE_DIABETES, DIABETES_MELLITUS_TYPE_1,
            DIABETES_MELLITUS_TYPE_2, GESTATIONAL_DIABETES);
    public static final List<String> HTM_CONFIRM_DIAGNOSIS = List.of(HYPER_TENSION, PRE_HYPER_TENSION);
    public static final String CLIENT_SPICE_MOBILE = "spice mobile";
    public static final String CLIENT_SPICE_WEB = "spice web";
    public static final String CLIENT_CFR_WEB = "cfr web";
    public static final String ROLE_HEALTH_COACH = "HEALTH_COACH";
    public static final String ROLE_HEALTH_SCREENER = "HEALTH_SCREENER";
    public static final String ROLE_HRIO = "HRIO";
    public static final String ROLE_LAB_TECHNICIAN = "LAB_TECHNICIAN";
    public static final String ROLE_NURSE = "NURSE";
    public static final String ROLE_NUTRITIONIST = "NUTRITIONIST";
    public static final String ROLE_PHARMACIST = "PHARMACIST";
    public static final String ROLE_PHYSICIAN_PRESCRIBER = "PHYSICIAN_PRESCRIBER";
    public static final String ROLE_PROVIDER = "PROVIDER";
    public static final String ROLE_RED_RISK_USER = "RED_RISK_USER";
    public static final Map<String, String> SPICE_MOBILE_ROLES = Map.of(ROLE_HEALTH_COACH, ROLE_HEALTH_COACH,
            ROLE_HEALTH_SCREENER, ROLE_HEALTH_SCREENER, ROLE_HRIO, ROLE_HRIO, ROLE_LAB_TECHNICIAN, ROLE_LAB_TECHNICIAN,
            ROLE_NURSE, ROLE_NURSE, ROLE_NUTRITIONIST, ROLE_NUTRITIONIST, ROLE_PHARMACIST, ROLE_PHARMACIST,
            ROLE_PROVIDER, ROLE_PROVIDER, ROLE_PHYSICIAN_PRESCRIBER, ROLE_PHYSICIAN_PRESCRIBER);
    public static final String ROLE_ACCOUNT_ADMIN = "ACCOUNT_ADMIN";
    public static final String ROLE_OPERATING_UNIT_ADMIN = "OPERATING_UNIT_ADMIN";
    public static final String ROLE_REGION_ADMIN = "REGION_ADMIN";
    public static final String ROLE_REPORT_ADMIN = "REPORT_ADMIN";
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ROLE_SYSTEM_ADMIN = "SYSTEM_ADMIN";
    public static final String ROLE_SUPER_USER = "SUPER_USER";
    public static final String EMR_ACCOUNT_ADMIN = "EMR_ACCOUNT_ADMIN";
    public static final String EMR_FACILITY_ADMIN = "EMR_FACILITY_ADMIN";
    public static final String EMR_OPERATING_UNIT_ADMIN = "EMR_OPERATING_UNIT_ADMIN";
    public static final String EMR_REGION_ADMIN = "EMR_REGION_ADMIN";
    public static final String EMR_REPORT_ADMIN = "EMR_REPORT_ADMIN";
    public static final Map<String, String> SPICE_WEB_ROLES = Map.of(ROLE_ACCOUNT_ADMIN, ROLE_ACCOUNT_ADMIN,
            ROLE_OPERATING_UNIT_ADMIN, ROLE_OPERATING_UNIT_ADMIN, ROLE_REGION_ADMIN, ROLE_REGION_ADMIN,
            ROLE_REPORT_ADMIN, ROLE_REPORT_ADMIN, ROLE_SUPER_ADMIN, ROLE_SUPER_ADMIN, ROLE_SYSTEM_ADMIN,
            ROLE_SYSTEM_ADMIN, ROLE_SUPER_USER, ROLE_SUPER_USER);
    public static final Map<String, String> SPICE_EMR_ROLES = Map.of(EMR_ACCOUNT_ADMIN, EMR_ACCOUNT_ADMIN,
            EMR_FACILITY_ADMIN, EMR_FACILITY_ADMIN, EMR_OPERATING_UNIT_ADMIN, EMR_OPERATING_UNIT_ADMIN,
            EMR_REGION_ADMIN, EMR_REGION_ADMIN, EMR_REPORT_ADMIN, EMR_REPORT_ADMIN);
    public static final String ROLE_EMR_REPORT_ADMIN = "EMR_REPORT_ADMIN";
    public static final String ROLE_EMR_REGION_ADMIN = "EMR_REGION_ADMIN";
    public static final String ROLE_EMR_ACCOUNT_ADMIN = "EMR_ACCOUNT_ADMIN";
    public static final String ROLE_EMR_OPERATING_UNIT_ADMIN = "EMR_OPERATING_UNIT_ADMIN";
    public static final String ROLE_EMR_SITE_ADMIN = "EMR_SITE_ADMIN";
    public static final Map<String, String> CFR_WEB_ROLES = Map.of(ROLE_EMR_REPORT_ADMIN, ROLE_EMR_REPORT_ADMIN,
            ROLE_EMR_REGION_ADMIN, ROLE_EMR_REGION_ADMIN, ROLE_EMR_ACCOUNT_ADMIN, ROLE_EMR_ACCOUNT_ADMIN,
            ROLE_EMR_OPERATING_UNIT_ADMIN, ROLE_EMR_OPERATING_UNIT_ADMIN, ROLE_EMR_SITE_ADMIN, ROLE_EMR_SITE_ADMIN);
    public static final String TOTAL_COUNT = "totalCount";
    public static final String INPUT_ID = "in_id";
    public static final String INPUT_TENANT_ID = "in_tenant_id";
    public static final String UPDATE_VIRTUAL_ID = "update_virtual_id";
    public static final String LABLE_NAME = "label_name";
    public static final String CLINICAL = "clinical";
    public static final String COUNT_PRESCRIPTION_DAYS_COMPLETED = "prescriptionDaysCompletedCount";
    public static final String COUNT_NUTRITION_LIFESTYLE_REVIED = "nutritionLifestyleReviewedCount";
    public static final String COUNT_NON_REVIED_TEST = "nonReviewedTestCount";
    public static final String ID = "id";
    public static final String VISIT_DATE = "visitDate";
    public static final String COUNTRY_ID = "countryId";
    public static final String COUNTRY_IDS = "countryIds";
    public static final String ACCOUNT_ID = "accountId";
    public static final String ACCOUNT_IDS = "accountIds";
    public static final String OPERATING_UNIT_ID = "operatingUnitId";
    public static final String OPERATING_UNIT_IDS = "operatingUnitIds";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String SITE = "site";
    public static final String REGION_CUSTOMIZATION_TYPES = "regionCustomizationTypes";
    public static final String REGION_CONSENT_FORM_TYPES = "regionConsentFormTypes";
    public static final String SITE_IDS = "siteIds";
    public static final String IS_PASSWORD_SET = "isPasswordSet";
    public static final String EXPIRES = "?expires=";
    public static final String RESET_PASSWORD = "&reset_password=true";
    public static final String CVD_RISK_HIGH = "HIGH";
    public static final String CVD_RISK_LOW = "LOW";
    public static final String CVD_RISK_MEDIUM = "MEDIUM";
    public static final Map<String, List<String>> CVD_RISK = Map.of(CVD_RISK_LOW,
            new ArrayList<>(List.of("Low risk")), CVD_RISK_HIGH, new ArrayList<>(List.of("High risk")),
            CVD_RISK_MEDIUM, new ArrayList<>(Arrays.asList("Medium risk", "Medium high risk")));
    public static final String REF_ID = "refId";
    // old screening log fields
    public static final String NATIONAL_ID = "national_id";
    public static final String PHONE_NUMBER_CATEGORY = "phone_number_category";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String LANDMARK = "landmark";
    public static final String FIRST_NAME = "first_name";
    public static final String AVG_SYSTOLIC = "avgSystolic";
    public static final String AVG_DIASTOLIC = "avg_diastolic";
    public static final String BPLOG_DETAILS = "bplog_details";
    public static final String GLUCOSE_VALUE = "glucoseValue";
    public static final String GLUCOSE_TYPE = "glucose_type";
    public static final String GLUCOSE_DATE_TIME = "glucose_date_time";
    public static final String BLOOD_GLUCOSE_UNIT = "blood_glucose_unit";
    public static final String AGE = "age";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String BMI = "bmi";
    public static final String IS_REGULAR_SMOKER = "is_regular_smoker";
    public static final String GENDER = "gender";
    public static final String SPICE = "spice";
    public static final String LOGIN = "login";
    public static final String PATIENT_TRACK_ID = "patientTrackId";
    public static final String PATIENT_VISIT_ID = "patientVisitId";
    public static final String ACTIVATE = "Activate";
    public static final String DEACTIVATE = "Deactivate";
    public static final String LIST_ARRAY = "list-array";
    public static final String COLUMN_DEFINITION_TEXT = "text[]";
    public static final int MINUS_ONE = -1;
    public static final long MINUS_ONE_LONG = -1;
    public static final String CATEGORY_TWO = "category2";
    public static final String CATEGORY_THREE = "category3";
    public static final String CATEGORY_FOUR = "category4";
    public static final String CATEGORY_FIVE = "category5";
    public static final String CATEGORY_TWO_COUNT = "category2count";
    public static final String CATEGORY_THREE_COUNT = "category3count";
    public static final String CATEGORY_FOUR_COUNT = "category4count";
    public static final String CATEGORY_FIVE_COUNT = "category5count";
    public static final String COLUMN_DEFINITION_VARCHAR = "varchar[]";
    public static final String MEDICAL_REVIEW_START_DATE = "medicalReviewStartDate";
    public static final String MEDICAL_REVIEW_END_DATE = "medicalReviewEndDate";
    public static final String ASSESSMENT_START_DATE = "assessmentStartDate";
    public static final String ASSESSMENT_END_DATE = "assessmentEndDate";
    public static final String PATIENT_STATUS_NOT_SCREENED = "patientStatusNotScreened";
    public static final String PATIENT_STATUS_ENROLLED = "patientStatusEnrolled";
    public static final String PATIENT_STATUS_NOT_ENROLLED = "patientStatusNotEnrolled";
    public static final String NEXT_MEDICAL_REVIEW_START_DATE = "nextMedicalReviewStartDate";
    public static final String NEXT_MEDICAL_REVIEW_END_DATE = "nextMedicalReviewEndDate";
    public static final String LAST_ASSESSMENT_START_DATE = "lastAssessmentStartDate";
    public static final String LAST_ASSESSMENT_END_DATE = "lastAssessmentEndDate";
    public static final String IS_RED_RISK_PATIENT = "isRedRiskPatient";
    public static final String SCREENING_REFERRAL = "screeningReferral";
    public static final String IS_LAB_TEST_REFERRED = "isLabTestReferred";
    public static final String IS_MEDICATION_PRESCRIBED = "isMedicationPrescribed";
    public static final String PARAM_FIRST_NAME = "firstName";
    public static final String PARAM_LAST_NAME = "lastName";
    public static final String PARAM_PHONE_NUMBER = "phoneNumber";
    public static final String CVD_RISK_LEVEL = "cvdRiskLevel";
    public static final String PARAM_NATIONAL_ID = "nationalId";
    public static final String PROGRAM_ID = "programId";
    public static final String IS_DELETED = "isDeleted";
    public static final String LAST_ASSESSMENT_DATE = "lastAssessmentDate";
    public static final String NEXT_MEDICAL_REVIEW_DATE = "nextMedicalReviewDate";
    public static final String NEXT_BP_ASSESSMENT_DATE = "nextBpAssessmentDate";
    public static final String NEXT_BG_ASSESSMENT_DATE = "nextBgAssessmentDate";
    public static final String CVD_RISK_SCORE = "cvdRiskScore";
    public static final String MEDICATION_PRESCRIBED_START_DATE = "medicationPrescribedStartDate";
    public static final String MEDICATION_PRESCRIBED_END_DATE = "medicationPrescribedEndDate";
    public static final String LABTEST_REFERRED_START_DATE = "labTestReferredStartDate";
    public static final String LABTEST_REFERRED_END_DATE = "labTestReferredEndDate";
    public static final String TODAY = "today";
    public static final String TODAY_START_DATE = "todayStartDate";
    public static final String TODAY_END_DATE = "todayEndDate";
    public static final String TOMORROW = "tomorrow";
    public static final String TOMORROW_START_DATE = "tomorrowStartDate";
    public static final String TOMORROW_END_DATE = "tomorrowEndDate";
    public static final String YESTERDAY = "yesterday";
    public static final String YESTERDAY_START_DATE = "yesterdayStartDate";
    public static final String YESTERDAY_END_DATE = "yesterdayEndDate";
    public static final String ZONED_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSxxx";
    public static final String PATIENT_SEARCH_REGEX = "^[0-9a-zA-Z@._-]+$";
    public static final String NUMBER_REGEX = "[0-9]+";
    public static final String SCREEN_TYPES = "screenTypes";
    public static final String CLINICAL_WORKFLOW_ID = "clinicalWorkflowId";
    public static final String REGEX_SEARCH_PATTERN = "[^a-zA-Z0-9]*";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String FORM = "form";
    public static final String DISPLAY_ORDER = "displayOrder";
    public static final String CATEGORY = "category";
    public static final String FORM_LAYOUT = "formLayout";
    public static final String FAMILY = "family";
    public static final String SITE_ID = "siteId";
    public static final String IDS = "ids";
    public static final String REMAINING_PRESCRIPTION_DAYS = "remainingPrescriptionDays";
    public static final String PRESCRIPTION_FILLED_DAYS = "prescriptionFilledDays";
    public static final String PRESCRIPTION_ID = "prescriptionId";
    public static final String PRESCRIPTION_REQUEST = "prescriptionRequest";
    public static final String SIGNATURE_FILE = "signatureFile";
    public static final String PRESCRIPTION_FORMAT = "prescsign.jpeg";
    // Patient transfer
    public static final String TRANSFERSTATUS = "transferStatus";
    public static final String TRANSFERREASON = "transferReason";
    public static final String PATIENT = "patient";
    public static final String TRANSFERBY = "transferBy";
    public static final String TRANSFERTO = "transferTo";
    public static final String OLDSITE = "oldSite";
    public static final String TRANSFERSITE = "transferSite";
    public static final String PROVISIONALDIAGNOSIS = "provisionalDiagnosis";
    public static final String CONFIRMDIAGNOSIS = "confirmDiagnosis";
    public static final String CVDRISKLEVEL = "cvdRiskLevel";
    public static final String CVDRISKSCORE = "cvdRiskScore";
    public static final String ISREDRISKPATIENT = "isRedRiskPatient";
    public static final String ENROLLMENTAT = "enrollmentAt";
    public static final String LASTMENSTRUALPERIODDATE = "lastMenstrualPeriodDate";
    public static final String ESTIMATEDDELIVERYDATE = "estimatedDeliveryDate";
    public static final String PREGNANCYDETAILS = "pregnancyDetails";
    public static final String FORM_DATA_ID = "formDataId";
    public static final String FORM_NAME = "formName";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String MATCH_LEVEL = "matchLevel";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String LOCATION_ID = "locationId";
    public static final String SUGGESTIONS = "suggestions";
    public static final String QUERY = "&query=";
    public static final String LOCATION_ID_URL = "&locationId=";
    public static final String LOCATION = "Location";
    public static final String MAX_RESULT_LIMIT = "&maxresults=20";
    public static final String RESPONSE = "Response";
    public static final String VIEW = "View";
    public static final String RESULT = "Result";
    public static final String COUNTY = "County";
    public static final String DISPLAY_POSITION = "DisplayPosition";
    public static final String ADDRESS = "Address";
    public static final String OSM_CITY_NAME_URL = "https://nominatim.openstreetmap" +
            ".org/search?format=json&accept-language=en&q=";

    public static final String OSM_PLACE_ID_URL = "https://nominatim.openstreetmap.org/details" +
            ".php?format=json&accept-language=en&place_id=";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";
    public static final String DATE_FORMAT_OLD_SCREENING_LOG = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String BP_TAKEN_ON = "bpTakenOn";
    public static final String BG_TAKEN_ON = "bgTakenOn";
    public static final String CULTURE_VALUE = "cultureValue";
    public static final String MEDICATION_UNIQUE_KEY = "medication_unique_key";
    public static final List<String> COMMON_DIABETES_CONFIRM_DIAGNOSIS = List.of(PRE_DIABETES,
            DIABETES_MELLITUS_TYPE_1, DIABETES_MELLITUS_TYPE_2, GESTATIONAL_DIABETES);
    public static final String ENTITY_LIST = "entityList";
    public static final Map<String, String> ROLES = Map.of(ROLE_HRIO, "HRIO", ROLE_HEALTH_COACH, "Health Coach",
            ROLE_HEALTH_SCREENER, "Health Screener", ROLE_PHYSICIAN_PRESCRIBER, "Physician Prescriber", ROLE_PROVIDER,
            "Provider", ROLE_LAB_TECHNICIAN, "Lab Technician", ROLE_RED_RISK_USER, "Red Risk User", ROLE_PHARMACIST,
            "Pharmacist", ROLE_NUTRITIONIST, "Nutritionist", ROLE_NURSE, "Nurse");
    public static final String NUTRITION_LIFESTYLE = "NutritionLifestyle";
    public static final String DOSAGE_FORM = "DosageForm";
    public static final String CULTURE_VALUE_SYMPTOMS = "Symptoms";
    public static final String CULTURE_VALUE_MEDICAL_COMPLIANCES = "MedicalCompliance";
    public static final String CULTURE_VALUE_DIAGNOSIS = "Diagnosis";
    public static final String CULTURE_VALUE_REASONS = "Reasons";
    public static final String CULTURE_VALUE_COMORBIDITIES = "Comorbidity";
    public static final String CULTURE_VALUE_COMPLAINTS = "Complaints";
    public static final String CULTURE_VALUE_CURRENT_MEDICATION = "CurrentMedication";
    public static final String CULTURE_VALUE_COMPLICATIONS = "Complication";
    public static final String CULTURE_VALUE_PHYSICAL_EXAMINATION = "PhysicalExamination";
    public static final String CULTURE_VALUE_LIFESTYLE_QUESTIONS = "LifestyleQuestions";
    public static final String CULTURE_VALUE_LIFESTYLE_ANSWERS = "LifestyleAnswers";
    public static final String CULTURE_VALUE_FREQUENCY = "Frequency";
    public static final String CULTURE_VALUE_MODEL_QUESTIONS = "ModelQuestions";
    public static final String CULTURE_VALUE_SIDE_MENU = "SideMenu";
    public static final String DEFAULT_CULTURE_VALUE = "English - India";
    public static final String CULTURE_VALUE_FREQUENCY_TYPE = "FrequencyType";
    public static final String VIEW_SCREENS = "viewScreens";
    public static final String FORM_INPUT = "formInput";
    public static final String ORGANIZATION_REDIS_KEY = "organization";
    public static final String CULTURE_ID = "cultureId";
    public static final Long LONG_ZERO = 0L;
    public static final String MODULE_TYPE = "moduleType";
    public static final String NEXT_MEDICAL_REVIEW_NOTIFICATION = "NEXT_MEDICAL_REVIEW_NOTIFICATION";
    public static final String NEXT_BP_ASSESSMENT_NOTIFICATION = "NEXT_BP_ASSESSMENT_NOTIFICATION";
    public static final String NEXT_BG_ASSESSMENT_NOTIFICATION = "NEXT_BG_ASSESSMENT_NOTIFICATION";
    public static final List<String> TEMPLATE_LIST = List.of(NEXT_MEDICAL_REVIEW_NOTIFICATION,
            NEXT_BP_ASSESSMENT_NOTIFICATION, NEXT_BG_ASSESSMENT_NOTIFICATION);
    public static final String OUTBOUND_NAME = "{{name}}";
    public static final String OUTBOUND_SITE_NAME = "{{site_name}}";
    public static final String LOCALNAME = "localname";
    public static final String GEOMETRY = "geometry";
    public static final String COORDINATES = "coordinates";
    public static final String ADDRESSTYPE = "addresstype";
    public static final String DISPLAY_NAME = "display_name";
    public static final String PLACE_ID = "place_id";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String BROWSER = "browser";
    public static final String SITE_DATA = "Site_Data";
    public static final String ORGANIZATION_LOGGER = "In OrganizationServiceImpl, setAndSendFhirSiteRequest :: ";
    public static final String OBJECT_TO_STRING_LOGGER = "ERROR Converting from Object to String :: ";
    public static final String RABBIT_MQ_LOGGER = "ERROR Connecting to RabbitMQ Queue :: ";
    public static final String ERROR_LOGGER = "ERROR in setAndSendFhirSiteRequest :: ";
    public static final String USER_LOGGER = "In UserServiceImpl, setAndSendFhirUserRequest :: ";
    public static final String FHIR_USER_DATA = "User_Data";
    public static List<Complaints> COMPLAINTS = new ArrayList<>();
    public static List<NutritionLifestyle> NUTRITION_LIFESTYLES = new ArrayList<>();
    public static List<PhysicalExamination> PHYSICAL_EXAMINATIONS = new ArrayList<>();
    public static List<Frequency> FREQUENCY_LIST = new ArrayList<>();
    public static List<Lifestyle> LIFESTYLES = new ArrayList<>();
    public static List<DosageForm> DOSAGE_FORMS = new ArrayList<>();
    public static List<Diagnosis> DIAGNOSIS = new ArrayList<>();
    public static List<DosageFrequency> DOSAGE_FREQUENCIES = new ArrayList<>();
    public static List<MedicalCompliance> MEDICAL_COMPLIANCES = new ArrayList<>();
    public static List<ModelQuestions> MODEL_QUESTIONS = new ArrayList<>();
    public static List<RiskAlgorithm> RISK_ALGORITHMS = new ArrayList<>();
    public static List<Reason> REASONS = new ArrayList<>();
    public static List<Symptom> SYMPTOMS = new ArrayList<>();
    public static List<SideMenu> SIDE_MENUS = new ArrayList<>();
    public static List<Comorbidity> COMORBIDITIES = new ArrayList<>();
    public static List<Complication> COMPLICATIONS = new ArrayList<>();
    public static List<FormMetaUi> FORM_META_UIS = new ArrayList<>();
    public static Map<Long, Map<String, Map<Long, String>>> CULTURE_VALUES_MAP = new HashMap<>();
    public static Map<Long, Map<String, Map<Long, Object>>> JSON_CULTURE_VALUES_MAP = new HashMap<>();
    public static List<CultureDTO> CULTURE_LIST = new ArrayList<>();
    public static List<FrequencyType> FREQUENCY_TYPE = new ArrayList<>();
    public static final String SPACE_COMMA_SPACE_SPLIT_PATTERN = "\\s*,\\s*";
    public static final String SHA_256 = "SHA-256";
    public static final String NO_SUCH_ALGORITHM = "No such algorithm Exception occurred : \n";
    public static final String TYPE_NOT_NULL_MESSAGE = "Type should not be null";
    public static final String ENROLLMENT_DATA = "Enrollment_Data";
    public static final String DEDUPLICATION_ID = "deduplicationId";
    public static final String BODY = "body";
    public static final String ASSESSMENT_DATA = "Assessment_Data";

    private Constants() {
    }
}
