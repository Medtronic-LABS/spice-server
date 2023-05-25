package com.mdtlabs.coreplatform.spiceservice.patienttracker.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface PatientTrackerRepository extends JpaRepository<PatientTracker, Long> {

    String FROM_PATIENT_TRACKER = "FROM PatientTracker as pTracker";

    String GET_PATIENT_TRACKER = "from PatientTracker as tracker where tracker.id=:id and tracker.patientStatus=:status and tracker.tenantId=:tenantId and tracker.isDeleted=false";

    String GET_MY_PATIENTS_LIST = FROM_PATIENT_TRACKER
            + "  WHERE (:nextMedicalReviewStartDate IS null OR pTracker.nextMedicalReviewDate >= CAST(:nextMedicalReviewStartDate AS timestamp))"
            + "  AND (:nextMedicalReviewEndDate IS null OR pTracker.nextMedicalReviewDate <= CAST(:nextMedicalReviewEndDate AS timestamp))"
            + "  AND (:lastAssessmentStartDate IS null OR pTracker.nextBpAssessmentDate >= CAST(:lastAssessmentStartDate AS timestamp))"
            + "  AND (:lastAssessmentEndDate IS null OR pTracker.nextBpAssessmentDate <= CAST(:lastAssessmentEndDate AS timestamp))"
            + "  AND (:medicationPrescribedStartDate IS null OR pTracker.lastMedicationPrescribedDate >= CAST(:medicationPrescribedStartDate AS timestamp))"
            + "  AND (:medicationPrescribedEndDate IS null OR pTracker.lastMedicationPrescribedDate <= CAST(:medicationPrescribedEndDate AS timestamp))"
            + "  AND (:labTestReferredStartDate IS null OR pTracker.lastLabtestReferredDate >= CAST(:labTestReferredStartDate AS timestamp))"
            + "  AND (:labTestReferredEndDate IS null OR pTracker.lastLabtestReferredDate <= CAST(:labTestReferredEndDate AS timestamp))"
            + "  AND (:isRedRiskPatient IS null OR pTracker.isRedRiskPatient = :isRedRiskPatient)"
            + "  AND ((:cvdRiskLevel) IS null OR pTracker.cvdRiskLevel in (:cvdRiskLevel))"
            + "  AND ((:screeningReferral IS null OR pTracker.screeningReferral = :screeningReferral)"
            + "  OR (:patientStatusNotScreened IS null OR pTracker.patientStatus != :patientStatusNotScreened) )"
            + "  AND (:isLabTestReferred IS null OR pTracker.isLabtestReferred = :isLabTestReferred)"
            + "  AND (:isMedicationPrescribed IS null OR pTracker.isMedicationPrescribed = :isMedicationPrescribed)"
            + "  AND (:patientStatusEnrolled IS null OR pTracker.patientStatus = :patientStatusEnrolled)"
            + "  AND (:patientStatusNotEnrolled IS null OR pTracker.patientStatus != :patientStatusNotEnrolled)"
            + " AND pTracker.isDeleted=false AND pTracker.tenantId=:tenantId";
    String SEARCH_PATIENTS = FROM_PATIENT_TRACKER
            + " WHERE (:tenantId IS null OR pTracker.tenantId = :tenantId) "
            + " AND (:operatingUnitId IS null OR pTracker.operatingUnitId = :operatingUnitId)"
            + " AND (:nextMedicalReviewStartDate IS null OR pTracker.nextMedicalReviewDate >= CAST(:nextMedicalReviewStartDate timestamp))"
            + " AND (:nextMedicalReviewEndDate IS null OR pTracker.nextMedicalReviewDate <= CAST(:nextMedicalReviewEndDate timestamp))"
            + " AND (:lastAssessmentStartDate IS null OR pTracker.nextBpAssessmentDate >= CAST(:lastAssessmentStartDate timestamp))"
            + " AND (:lastAssessmentEndDate IS null OR pTracker.nextBpAssessmentDate <= CAST(:lastAssessmentEndDate timestamp))"
            + " AND (:medicationPrescribedStartDate IS null OR pTracker.lastMedicationPrescribedDate >= CAST(:medicationPrescribedStartDate AS timestamp))"
            + " AND (:medicationPrescribedEndDate IS null OR pTracker.lastMedicationPrescribedDate <= CAST(:medicationPrescribedEndDate AS timestamp))"
            + " AND (:labTestReferredStartDate IS null OR pTracker.lastLabtestReferredDate >= CAST(:labTestReferredStartDate AS timestamp))"
            + " AND (:labTestReferredEndDate IS null OR pTracker.lastLabtestReferredDate <= CAST(:labTestReferredEndDate AS timestamp))"
            + " AND (:isRedRiskPatient IS null OR pTracker.isRedRiskPatient = :isRedRiskPatient)"
            + " AND ((:cvdRiskLevel) IS null OR pTracker.cvdRiskLevel in (:cvdRiskLevel))"
            + " AND ((:screeningReferral IS null OR pTracker.screeningReferral = :screeningReferral)"
            + " OR (:patientStatusNotScreened IS null OR pTracker.patientStatus != :patientStatusNotScreened))"
            + " AND (:isLabTestReferred IS null OR pTracker.isLabtestReferred = :isLabTestReferred)"
            + " AND (:isMedicationPrescribed IS null OR pTracker.isMedicationPrescribed = :isMedicationPrescribed)"
            + " AND (:patientStatusEnrolled IS null OR pTracker.patientStatus = :patientStatusEnrolled)"
            + " AND (:patientStatusNotEnrolled IS null OR pTracker.patientStatus != :patientStatusNotEnrolled)  AND pTracker.isDeleted = false"
            + " AND ((:programId IS null AND pTracker.nationalId = :nationalId)"
            + " OR (:programId IS NOT null AND (pTracker.programId = :programId OR pTracker.nationalId = :nationalId)))";
    String ADVANCE_SEARCH_PATIENTS = FROM_PATIENT_TRACKER
            + " WHERE (:firstName IS null OR pTracker.firstName LIKE CONCAT(:firstName, '%'))"
            + " AND (:lastName IS null OR pTracker.lastName LIKE CONCAT(:lastName, '%'))"
            + " AND (:phoneNumber IS null OR pTracker.phoneNumber = :phoneNumber)"
            + " AND (:nextMedicalReviewStartDate IS null "
            + " OR pTracker.nextMedicalReviewDate >= CAST(:nextMedicalReviewStartDate timestamp))"
            + " AND (:nextMedicalReviewEndDate IS null "
            + " OR pTracker.nextMedicalReviewDate <= CAST(:nextMedicalReviewEndDate timestamp))"
            + " AND (:lastAssessmentStartDate IS null"
            + " OR pTracker.nextBpAssessmentDate >= CAST(:lastAssessmentStartDate timestamp))"
            + " AND (:lastAssessmentEndDate IS null "
            + " OR pTracker.nextBpAssessmentDate <= CAST(:lastAssessmentEndDate timestamp))"
            + " AND (:medicationPrescribedStartDate IS null OR pTracker.lastMedicationPrescribedDate >= CAST(:medicationPrescribedStartDate AS timestamp))"
            + " AND (:medicationPrescribedEndDate IS null OR pTracker.lastMedicationPrescribedDate <= CAST(:medicationPrescribedEndDate AS timestamp))"
            + " AND (:labTestReferredStartDate IS null OR pTracker.lastLabtestReferredDate >= CAST(:labTestReferredStartDate AS timestamp))"
            + " AND (:labTestReferredEndDate IS null OR pTracker.lastLabtestReferredDate <= CAST(:labTestReferredEndDate AS timestamp))"
            + " AND (:isRedRiskPatient IS null OR pTracker.isRedRiskPatient = :isRedRiskPatient)"
            + " AND ((:cvdRiskLevel) IS null OR pTracker.cvdRiskLevel in (:cvdRiskLevel))"
            + " AND ((:screeningReferral IS null OR pTracker.screeningReferral = :screeningReferral)"
            + " OR (:patientStatusNotScreened IS null OR pTracker.patientStatus != :patientStatusNotScreened) )"
            + " AND (:patientStatusEnrolled IS null OR pTracker.patientStatus = :patientStatusEnrolled)"
            + " AND (:patientStatusNotEnrolled IS null OR pTracker.patientStatus != :patientStatusNotEnrolled)"
            + " AND (:isLabTestReferred IS null OR pTracker.isLabtestReferred = :isLabTestReferred)"
            + " AND (:isMedicationPrescribed IS null OR pTracker.isMedicationPrescribed = :isMedicationPrescribed)"
            + " AND (:countryId IS null OR pTracker.countryId = :countryId)"
            + " AND (:operatingUnitId IS null OR pTracker.operatingUnitId = :operatingUnitId)"
            + " AND (:tenantId IS null OR pTracker.tenantId = :tenantId)"
            + " AND pTracker.isDeleted = :isDeleted ";

    String GET_FOLLOWUP_DATES = FROM_PATIENT_TRACKER
            + " WHERE ((:nextMedicalReviewDate IS NULL) OR ((:nextBpAssessmentDate IS NOT NULL) AND pTracker.nextMedicalReviewDate >= CAST(:startDate AS timestamp) AND pTracker.nextMedicalReviewDate <= CAST(:endDate AS timestamp)))"
            + " AND ((:nextBpAssessmentDate IS NULL) OR ((:nextBpAssessmentDate IS NOT NULL) AND pTracker.nextBpAssessmentDate >= CAST(:startDate AS timestamp) AND pTracker.nextBpAssessmentDate <= CAST(:endDate AS timestamp)))"
            + " AND ((:nextBgAssessmentDate IS NULL) OR ((:nextBgAssessmentDate IS NOT NULL) AND pTracker.nextBgAssessmentDate >= CAST(:startDate AS timestamp) AND pTracker.nextBgAssessmentDate <= CAST(:endDate AS timestamp)))";

    /**
     * <p>
     * Gets the list of patient tracker by follow-up dates.
     * </p>
     *
     * @param isNextMedicalReviewJob {@link Boolean} true or false
     * @param isNextBPJob            {@link Boolean} true or false
     * @param nextBGJob              {@link Boolean} true or false
     * @param startDate              {@link String} start date
     * @param endDate                {@link String} end date
     * @return List {@link List<PatientTracker>} list of patient tracker
     */
    @Query(value = GET_FOLLOWUP_DATES)
    List<PatientTracker> getFollowUpDates(@Param(Constants.NEXT_MEDICAL_REVIEW_DATE) Boolean isNextMedicalReviewJob, @Param(Constants.NEXT_BP_ASSESSMENT_DATE) Boolean isNextBPJob, @Param(Constants.NEXT_BG_ASSESSMENT_DATE) Boolean nextBGJob,
                                          @Param(Constants.START_DATE) String startDate, @Param(Constants.END_DATE) String endDate);

    /**
     * <p>
     * This function finds a patient tracker by their national ID, country ID, and whether or not they
     * have been deleted.
     * </p>
     *
     * @param nationalId {@link String} The national ID of the patient that we want to search for in the
     *                   PatientTracker system
     * @param country    {@link Long} The "country" parameter is a Long data type that represents the ID of the country
     *                   associated with the patient being searched for in the PatientTracker system.
     * @param isDeleted  {@link boolean} isDeleted is a boolean parameter that indicates whether the patient record has
     *                   been marked as deleted or not
     * @return {@link PatientTracker} The method `findByNationalIdAndCountryIdAndIsDeleted` returns an object of type
     * `PatientTracker` that matches the given `nationalId`, `country` and `isDeleted` parameters.
     */
    PatientTracker findByNationalIdAndCountryIdAndIsDeleted(String nationalId, Long country, boolean isDeleted);

    /**
     * <p>
     * This function finds a patient in a patient tracker by their ID and checks if they have been
     * deleted.
     * </p>
     *
     * @param id        {@link long} The unique identifier of the patient that we want to retrieve from the database.
     * @param isDeleted {@link boolean} The "isDeleted" parameter is a boolean value that indicates whether the patient
     *                  record has been marked as deleted or not is given
     * @return {@link PatientTracker} The method `findByIdAndIsDeleted` is returning an object of type `PatientTracker` that
     * matches the given `id` and `isDeleted` parameters is returned
     */
    PatientTracker findByIdAndIsDeleted(long id, boolean isDeleted);

    /**
     * <p>
     * This function retrieves a patient tracker based on the provided ID, status, and tenant ID
     * parameters.
     * </p>
     *
     * @param id       {@link Long} The ID parameter is a Long type parameter that is used to identify a specific patient
     *                 tracker
     * @param status   {@link String} The "status" parameter is a String type parameter that is used to filter the
     *                 results of the query based on the status of the patient tracker
     * @param tenantId {@link Long}  The tenantId parameter is a Long value that represents the unique identifier of
     *                 the tenant for which the patient tracker is being retrieved
     * @return {@link PatientTracker} The method is returning a single object of type `PatientTracker` based on the provided
     * parameters `id`, `status`, and `tenantId`.
     */
    @Query(value = GET_PATIENT_TRACKER)
    PatientTracker getPatientTracker(@Param(Constants.ID) Long id, @Param(FieldConstants.STATUS) String status,
                                     @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId);

    /**
     * <p>
     * This is a Java function that retrieves a list of patients with pagination based on various
     * parameters.
     * </p>
     *
     * @param medicalReviewStartDate        {@link String} The start date for the next medical review of the patients.
     * @param medicalReviewEndDate          {@link String} The end date for the next medical review of a patient.
     * @param assessmentStartDate           {@link String} The start date for the last assessment of the patient.
     * @param assessmentEndDate             {@link String} The end date for the last assessment of the patient.
     * @param medicationPrescribedStartDate {@link String} This parameter is used to filter patients based on the
     *                                      start date of when medication was prescribed to them.
     * @param medicationPrescribedEndDate   {@link String} medicationPrescribedEndDate is a parameter of type String
     *                                      that represents the end date range for when medication was prescribed to a patient. It is used
     *                                      in a query to retrieve a list of patients with pagination based on various criteria such as
     *                                      medical review dates, assessment dates, medication prescribed dates, lab test referred
     * @param labTestReferredStartDate      {@link String} This parameter is a string representing the start date for lab
     *                                      test referrals. It is used in a query to retrieve a list of patients with pagination based on
     *                                      various criteria, including lab test referrals.
     * @param labTestReferredEndDate        {@link String} labTestReferredEndDate is a parameter of type String that
     *                                      represents the end date range for lab test referred. It is used in a query method to retrieve a
     *                                      list of patients with pagination based on various search criteria.
     * @param isRedRiskPatient              {@link Boolean} A boolean value indicating whether the patient is considered to be at
     *                                      red risk or not.
     * @param cvdRiskLevel                  {@link List<String>} cvdRiskLevel is a list of strings representing the different levels of
     *                                      cardiovascular disease (CVD) risk that a patient may have. This parameter is used in a query to
     *                                      filter patients based on their CVD risk level.
     * @param screeningReferral             {@link Boolean} A boolean value indicating whether the patient has been referred for
     *                                      screening or not.
     * @param isLabTestReferred             {@link Boolean} A boolean parameter that indicates whether the patient has been
     *                                      referred for a lab test or not.
     * @param isMedicationPrescribed        {@link Boolean} A boolean parameter that indicates whether the patient has been
     *                                      prescribed medication or not.
     * @param patientStatusNotScreened      {@link String} This parameter is used to filter patients based on their
     *                                      screening status. It is a string parameter that specifies the status of patients who have not
     *                                      been screened.
     * @param patientStatusEnrolled         {@link String} This parameter is used to filter the patients who are currently
     *                                      enrolled in a program or treatment.
     * @param patientStatusNotEnrolled      {@link String} This parameter is used to filter the patients who are not
     *                                      enrolled in a particular program or treatment.
     * @param tenantId                      {@link String} The ID of the tenant for which the patients list is being fetched.
     * @param pageable                      {@link Long} It is an object of the Pageable interface which provides pagination and sorting
     *                                      functionality for the query results. It contains information about the current page number, page
     *                                      size, and sorting criteria.
     * @return {@link Page<PatientTracker>} A Page of PatientTracker objects that match the specified criteria, with pagination
     * applied.
     */
    @Query(value = GET_MY_PATIENTS_LIST)
    Page<PatientTracker> getPatientsListWithPagination(
            @Param(Constants.NEXT_MEDICAL_REVIEW_START_DATE) String medicalReviewStartDate,
            @Param(Constants.NEXT_MEDICAL_REVIEW_END_DATE) String medicalReviewEndDate,
            @Param(Constants.LAST_ASSESSMENT_START_DATE) String assessmentStartDate,
            @Param(Constants.LAST_ASSESSMENT_END_DATE) String assessmentEndDate,
            @Param(Constants.MEDICATION_PRESCRIBED_START_DATE) String medicationPrescribedStartDate,
            @Param(Constants.MEDICATION_PRESCRIBED_END_DATE) String medicationPrescribedEndDate,
            @Param(Constants.LABTEST_REFERRED_START_DATE) String labTestReferredStartDate,
            @Param(Constants.LABTEST_REFERRED_END_DATE) String labTestReferredEndDate,
            @Param(Constants.IS_RED_RISK_PATIENT) Boolean isRedRiskPatient,
            @Param(Constants.CVD_RISK_LEVEL) List<String> cvdRiskLevel,
            @Param(Constants.SCREENING_REFERRAL) Boolean screeningReferral,
            @Param(Constants.IS_LAB_TEST_REFERRED) Boolean isLabTestReferred,
            @Param(Constants.IS_MEDICATION_PRESCRIBED) Boolean isMedicationPrescribed,
            @Param(Constants.PATIENT_STATUS_NOT_SCREENED) String patientStatusNotScreened,
            @Param(Constants.PATIENT_STATUS_ENROLLED) String patientStatusEnrolled,
            @Param(Constants.PATIENT_STATUS_NOT_ENROLLED) String patientStatusNotEnrolled,
            @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId, Pageable pageable);

    /**
     * <p>
     * This is a Java function that searches for patients with various filters and returns a paginated
     * result.
     * </p>
     *
     * @param tenantId                      {@link Long} The ID of the tenant (organization) for which the patients are being searched.
     * @param operatingUnitId               {@link Long} The ID of the operating unit for which the patients are being searched.
     * @param medicalReviewStartDate        {@link String} The start date for the next medical review of a patient.
     * @param medicalReviewEndDate          {@link String} The end date for the next medical review of a patient. It is a
     *                                      parameter used in a method for searching patients with pagination.
     * @param assessmentStartDate           {@link String} The start date for the last assessment of the patient.
     * @param assessmentEndDate             {@link String} The end date for the last assessment of a patient.
     * @param medicationPrescribedStartDate {@link String} This parameter is used to filter patients based on the start
     *                                      date of when medication was prescribed to them. It is a string parameter that should contain a
     *                                      date in the format "yyyy-MM-dd".
     * @param medicationPrescribedEndDate   {@link String} The end date for the period during which medication was
     *                                      prescribed to the patient.
     * @param labTestReferredStartDate      {@link String} The start date for when a patient was referred for a lab test.
     * @param labTestReferredEndDate        {@link String} The end date for the period during which a lab test was referred
     *                                      for the patient.
     * @param isRedRiskPatient              {@link Boolean} A boolean value indicating whether the patient is considered a red risk
     *                                      patient or not.
     * @param cvdRiskLevel                  {@link List<String>} cvdRiskLevel is a list of strings representing the different levels of
     *                                      cardiovascular disease (CVD) risk that a patient may have. This parameter is used in a method
     *                                      that searches for patients with specific criteria, including their CVD risk level.
     * @param screeningReferral             {@link Boolean} A boolean value indicating whether the patient has been referred for
     *                                      screening or not.
     * @param isLabTestReferred             {@link Boolean} A boolean parameter that indicates whether the patient has been referred
     *                                      for a lab test or not.
     * @param isMedicationPrescribed        {@link Boolean} A boolean parameter that indicates whether the patient has been
     *                                      prescribed medication or not.
     * @param patientStatusNotScreened      {@link String} This parameter is used to filter patients based on their
     *                                      screening status. It is a string parameter that can take on the values "screened" or "not
     *                                      screened". Patients who have not been screened will be returned if the value of this parameter is
     *                                      "not screened".
     * @param patientStatusEnrolled         {@link String} This parameter is used to filter patients based on their enrollment
     *                                      status in a program. It is a string parameter that specifies the status of enrolled patients.
     * @param patientStatusNotEnrolled      {@link String} This parameter is used to filter patients based on their
     *                                      enrollment status. It is a string parameter that specifies the status of patients who have not
     *                                      been enrolled in a particular program.
     * @param nationalId                    {@link String} The national ID of the patient being searched for.
     * @param programId                     {@link Long} The ID of the program for which patients are being searched.
     * @param pageable                      {@link Pageable} Pageable is an interface in Spring Data that provides pagination and sorting
     *                                      functionality for queries. It allows you to specify the page number, page size, and sorting
     *                                      criteria for the query results. The Pageable parameter in this method is used to specify the
     *                                      pagination and sorting options for the search results.
     * @return {@link Page<PatientTracker>} The method is returning a Page of PatientTracker objects that match the search criteria
     * specified in the method parameters
     */
    @Query(value = SEARCH_PATIENTS)
    Page<PatientTracker> searchPatientsWithPagination(@Param(Constants.TENANT_PARAMETER_NAME) Long tenantId,
                                                      @Param(Constants.OPERATING_UNIT_ID) Long operatingUnitId,
                                                      @Param(Constants.NEXT_MEDICAL_REVIEW_START_DATE) String medicalReviewStartDate,
                                                      @Param(Constants.NEXT_MEDICAL_REVIEW_END_DATE) String medicalReviewEndDate,
                                                      @Param(Constants.LAST_ASSESSMENT_START_DATE) String assessmentStartDate,
                                                      @Param(Constants.LAST_ASSESSMENT_END_DATE) String assessmentEndDate,
                                                      @Param(Constants.MEDICATION_PRESCRIBED_START_DATE) String medicationPrescribedStartDate,
                                                      @Param(Constants.MEDICATION_PRESCRIBED_END_DATE) String medicationPrescribedEndDate,
                                                      @Param(Constants.LABTEST_REFERRED_START_DATE) String labTestReferredStartDate,
                                                      @Param(Constants.LABTEST_REFERRED_END_DATE) String labTestReferredEndDate,
                                                      @Param(Constants.IS_RED_RISK_PATIENT) Boolean isRedRiskPatient,
                                                      @Param(Constants.CVD_RISK_LEVEL) List<String> cvdRiskLevel,
                                                      @Param(Constants.SCREENING_REFERRAL) Boolean screeningReferral,
                                                      @Param(Constants.IS_LAB_TEST_REFERRED) Boolean isLabTestReferred,
                                                      @Param(Constants.IS_MEDICATION_PRESCRIBED) Boolean isMedicationPrescribed,
                                                      @Param(Constants.PATIENT_STATUS_NOT_SCREENED) String patientStatusNotScreened,
                                                      @Param(Constants.PATIENT_STATUS_ENROLLED) String patientStatusEnrolled,
                                                      @Param(Constants.PATIENT_STATUS_NOT_ENROLLED) String patientStatusNotEnrolled,
                                                      @Param(Constants.PARAM_NATIONAL_ID) String nationalId, @Param(Constants.PROGRAM_ID) Long programId, Pageable pageable);

    /**
     * <p>
     * This is a Java function that retrieves a list of patients based on various search criteria and
     * returns them in a pageable format.
     * </p>
     *
     * @param firstName                     {@link String} The first name of the patient being searched for.
     * @param lastName                      {@link String} The last name of the patient being searched for in the database.
     * @param phoneNumber                   {@link String} The phone number of the patient being searched for in the database.
     * @param medicalReviewStartDate        {@link String} The start date for the next medical review of a patient.
     * @param medicalReviewEndDate          {@link String} The end date for the next medical review of a patient.
     * @param assessmentStartDate           {@link String} The start date for the last assessment of the patient.
     * @param assessmentEndDate             {@link String} The end date for the last assessment of a patient.
     * @param medicationPrescribedStartDate {@link String} This parameter is used to filter patients based on the
     *                                      start date of when medication was prescribed to them. It is used in a query to retrieve a page
     *                                      of PatientTracker objects that match the specified criteria.
     * @param medicationPrescribedEndDate   {@link String} medicationPrescribedEndDate is a parameter used in a query to
     *                                      retrieve patients based on the end date of when medication was prescribed to them. It is likely
     *                                      a date value in string format that is passed as a parameter to the query.
     * @param labTestReferredStartDate      {@link String} This parameter is a string representing the start date for lab
     *                                      test referred. It is used in a query to retrieve patients who have been referred for lab tests
     *                                      within a specific date range.
     * @param labTestReferredEndDate        {@link String} labTestReferredEndDate is a parameter used in a query to retrieve
     *                                      patients based on the end date of when they were referred for a lab test. It is a string
     *                                      parameter that represents the date in the format "yyyy-MM-dd".
     * @param isRedRiskPatient              {@link Boolean} A boolean parameter that indicates whether the patient is considered a
     *                                      red risk patient or not.
     * @param cvdRiskLevel                  {@link List<String>} A list of strings representing the CVD (cardiovascular disease) risk level
     *                                      of the patients to be searched.
     * @param screeningReferral             {@link Boolean} A boolean parameter that indicates whether the patient has been
     *                                      referred for screening or not.
     * @param patientStatusNotScreened      {@link String} This parameter is used to filter patients based on their
     *                                      screening status. It is a string parameter that can take the values "yes" or "no". If set to
     *                                      "yes", it will return patients who have not been screened yet. If set to "no", it will return
     *                                      patients who have already
     * @param patientStatusEnrolled         {@link String} This parameter is used to filter patients based on their enrollment
     *                                      status. It is a string parameter that specifies the status of patients who are currently
     *                                      enrolled in the program.
     * @param patientStatusNotEnrolled      {@link String} This parameter is used to filter patients who have not been
     *                                      enrolled in a program or study. It is a string value that can be used to specify the status of
     *                                      the patient, such as "not enrolled" or "pending enrollment".
     * @param isLabTestReferred             {@link Boolean} A boolean parameter that indicates whether the patient has been
     *                                      referred for a lab test or not.
     * @param isMedicationPrescribed        {@link Boolean} A boolean parameter that indicates whether the patient has been
     *                                      prescribed medication or not.
     * @param countryId                     {@link Long} The ID of the country to which the patients belong.
     * @param operatingUnitId               {@link Long} The ID of the operating unit to which the patients belong.
     * @param tenantId                      {@link Long} The ID of the tenant for which the query is being executed.
     * @param isDeleted                     {@link boolean} A boolean parameter that indicates whether the patient record has been deleted
     *                                      or not. If set to true, only non-deleted patient records will be returned in the query result.
     * @param pageable                      {@link Pageable} An object that represents pagination information such as page number, page size,
     *                                      and sorting criteria for the query results.
     * @return {@link Page<PatientTracker>} A Page of PatientTracker objects that match the search criteria specified in the method
     * parameters.
     */
    @Query(value = ADVANCE_SEARCH_PATIENTS)
    Page<PatientTracker> getPatientsWithAdvanceSearch(@Param(Constants.PARAM_FIRST_NAME) String firstName,
                                                      @Param(Constants.PARAM_LAST_NAME) String lastName, @Param(Constants.PARAM_PHONE_NUMBER) String phoneNumber,
                                                      @Param(Constants.NEXT_MEDICAL_REVIEW_START_DATE) String medicalReviewStartDate,
                                                      @Param(Constants.NEXT_MEDICAL_REVIEW_END_DATE) String medicalReviewEndDate,
                                                      @Param(Constants.LAST_ASSESSMENT_START_DATE) String assessmentStartDate,
                                                      @Param(Constants.LAST_ASSESSMENT_END_DATE) String assessmentEndDate,
                                                      @Param(Constants.MEDICATION_PRESCRIBED_START_DATE) String medicationPrescribedStartDate,
                                                      @Param(Constants.MEDICATION_PRESCRIBED_END_DATE) String medicationPrescribedEndDate,
                                                      @Param(Constants.LABTEST_REFERRED_START_DATE) String labTestReferredStartDate,
                                                      @Param(Constants.LABTEST_REFERRED_END_DATE) String labTestReferredEndDate,
                                                      @Param(Constants.IS_RED_RISK_PATIENT) Boolean isRedRiskPatient,
                                                      @Param(Constants.CVD_RISK_LEVEL) List<String> cvdRiskLevel,
                                                      @Param(Constants.SCREENING_REFERRAL) Boolean screeningReferral,
                                                      @Param(Constants.PATIENT_STATUS_NOT_SCREENED) String patientStatusNotScreened,
                                                      @Param(Constants.PATIENT_STATUS_ENROLLED) String patientStatusEnrolled,
                                                      @Param(Constants.PATIENT_STATUS_NOT_ENROLLED) String patientStatusNotEnrolled,
                                                      @Param(Constants.IS_LAB_TEST_REFERRED) Boolean isLabTestReferred,
                                                      @Param(Constants.IS_MEDICATION_PRESCRIBED) Boolean isMedicationPrescribed,
                                                      @Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.OPERATING_UNIT_ID) Long operatingUnitId,
                                                      @Param(Constants.IS_DELETED) boolean isDeleted, @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId, Pageable pageable);

}
