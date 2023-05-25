package com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This repository class is responsible for communication between database and
 * server side.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Repository
public interface PatientLabTestRepository extends JpaRepository<PatientLabTest, Long> {

    String GET_PATIENT_LAB_TEST_LIST = "SELECT p from PatientLabTest as p where "
            + "p.patientTrackId = :patientTrackId AND (:patientVisitId is null or p.patientVisitId = :patientVisitId) "
            + "AND p.isDeleted=:isDeleted";

    String GET_PATIENT_LAB_TEST_LIST_WITH_CONDITION = "SELECT p from PatientLabTest as p where "
            + "p.patientTrackId = :patientTrackId AND (:patientVisitId is null or p.patientVisitId = :patientVisitId) "
            + "AND p.isDeleted=:isDeleted AND p.resultUpdateBy is null";

    String GET_PATIENT_LAB_TEST_COUNT =
            "Select count(labtest.id) from PatientLabTest as labtest where labtest.isReviewed=false and labtest.isDeleted=false and labtest.resultUpdateBy is not null and labtest.patientTrackId=:patientTrackId";

    String GET_PATIENT_LAB_TEST_WITHOUT_RESULTS = "SELECT labtests from PatientLabTest as labtests "
            + "where labtests.resultDate is null AND labtests.patientTrackId =:patientTrackId AND labtests.tenantId"
            + "=:tenantId AND labtests.isDeleted = false";


    /**
     * <p>
     * This function retrieves a list of patient lab tests based on the patient's track ID, visit ID, and
     * whether they have been deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} This parameter is of type Long and is used to identify a specific patient's
     *                       track ID
     * @param patientVisitId {@link Long} The ID of the patient's visit for which the lab test list is being retrieved.
     * @param isDeleted      {@link Boolean} isDeleted is a boolean parameter that is used to filter the list of patient lab
     *                       tests based on whether they have been marked as deleted or not
     * @return {@link List<PatientLabTest>} A list of PatientLabTest objects is being returned.
     */
    @Query(value = GET_PATIENT_LAB_TEST_LIST)
    List<PatientLabTest> getPatientLabTestList(@Param("patientTrackId") Long patientTrackId,
                                               @Param("patientVisitId") Long patientVisitId,
                                               @Param("isDeleted") Boolean isDeleted);

    /**
     * <p>
     * This function retrieves a list of patient lab tests with a specific condition.
     * </p>
     *
     * @param patientTrackId {@link Long} This parameter is of type Long and is used to identify a specific patient's
     *                       track ID. It is likely used to filter lab test results for a specific patient.
     * @param patientVisitId {@link Long} The ID of the patient visit for which the lab test list is being retrieved.
     * @param isDeleted      {@link Boolean} isDeleted is a boolean parameter that is used to filter the list of patient lab
     *                       tests based on whether they have been marked as deleted or not
     * @return {@link List<PatientLabTest>} A list of PatientLabTest objects that match the specified conditions of patientTrackId,
     * patientVisitId, and isDeleted.
     */
    @Query(value = GET_PATIENT_LAB_TEST_LIST_WITH_CONDITION)
    List<PatientLabTest> getPatientLabTestListWithCondition(@Param("patientTrackId") Long patientTrackId,
                                                            @Param("patientVisitId") Long patientVisitId,
                                                            @Param("isDeleted") Boolean isDeleted);

    /**
     * <p>
     * This function finds a patient lab test by its ID and checks if it has been deleted.
     * </p>
     *
     * @param id        {@link Long} The unique identifier of the PatientLabTest object that we want to retrieve from the
     *                  database.
     * @param isDeleted {@link Boolean} The "isDeleted" parameter is a boolean value that indicates whether the patient
     *                  lab test record has been marked as deleted or not is given
     * @return {@link PatientLabTest} The method is returning a single instance of the `PatientLabTest` class that matches the
     * given `id` and `isDeleted` parameters.
     */
    PatientLabTest findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * <p>
     * This function finds all patient lab tests by patient visit ID and whether they are marked as
     * deleted or not.
     * </p>
     *
     * @param patientVisitId {@link Long} The ID of the patient visit for which we want to find all the patient lab
     *                       tests.
     * @param isDeleted      {@link Boolean} isDeleted is a boolean parameter that indicates whether the patient lab test
     *                       has been marked as deleted or not. If the value is true, it means that the patient lab test has
     *                       been deleted, and if the value is false, it means that the patient lab test is still active.
     * @return {@link List<PatientLabTest>} This method is returning a list of PatientLabTest objects that match the given
     * patientVisitId and isDeleted flag.
     */
    List<PatientLabTest> findAllByPatientVisitIdAndIsDeleted(Long patientVisitId, Boolean isDeleted);

    /**
     * <p>
     * This function retrieves a list of patient lab tests without results based on the patient's track
     * ID and tenant ID.
     * </p>
     *
     * @param patientTrackId {@link long} The patientTrackId parameter is a unique identifier for a specific
     *                       patient
     * @param tenantId       {@link Long} The tenantId parameter is a Long data type that represents the unique identifier
     *                       of the tenant or organization that the patient belongs to.
     * @return {@link List<PatientLabTest>} The method is returning a list of PatientLabTest objects that do not have any results,
     * based on the provided patientTrackId and tenantId parameters.
     */
    @Query(GET_PATIENT_LAB_TEST_WITHOUT_RESULTS)
    List<PatientLabTest> getPatientLabTestsWithoutResults(@Param("patientTrackId") long patientTrackId,
                                                          @Param("tenantId") Long tenantId);

    /**
     * <p>
     * This function retrieves the number of lab tests that have not been reviewed for a given patient.
     * </p>
     *
     * @param patientTrackId {@link Long} patientTrackId is a Long type parameter that represents the unique
     *                       identifier of a patient's track record
     * @return {@link int} The method is returning an integer value which represents the count of lab tests that
     * have not been reviewed for a specific patient identified by their track ID.
     */
    @Query(value = GET_PATIENT_LAB_TEST_COUNT)
    int getLabTestNoReviewedCount(@Param("patientTrackId") Long patientTrackId);


    /**
     * <p>
     * This function finds a patient lab test by its ID and tenant ID.
     * </p>
     *
     * @param id       {@link Long} The unique identifier of the patient lab test that you want to retrieve.
     * @param tenantId {@link Long} The tenantId parameter is a Long value that represents the unique identifier of
     *                 the tenant for which the patient lab test is being searched is given
     * @return {@link PatientLabTest} The method is returning a single instance of the `PatientLabTest` class that matches the
     * given `id` and `tenantId`.
     */
    PatientLabTest findByIdAndTenantId(Long id, Long tenantId);

    /**
     * <p>
     * This function finds a list of patient lab tests based on the patient's track ID and whether they
     * have been marked as deleted.
     * </p>
     *
     * @param patientTrackId {@link long} patientTrackId is a unique identifier for a patient's medical record or
     *                       history
     * @param isDeleted      {@link boolean} isDeleted is a boolean parameter that indicates whether the patient lab test
     *                       has been marked as deleted or not
     * @return {@link List<PatientLabTest>} The method `findByPatientTrackIdAndIsDeleted` is returning a list of `PatientLabTest`
     * objects that match the given `patientTrackId` and `isDeleted` boolean value.
     */
    List<PatientLabTest> findByPatientTrackIdAndIsDeleted(long patientTrackId, boolean isDeleted);

    /**
     * <p>
     * This function finds a list of patient lab tests based on the patient's track ID.
     * </p>
     *
     * @param patientTrackId {@link long} patientTrackId is a unique identifier for a patient's medical record or
     *                       history
     * @return {@link List<PatientLabTest>} The method `findByPatientTrackId` returns a list of `PatientLabTest` objects that match
     * the given `patientTrackId`.
     */
    List<PatientLabTest> findByPatientTrackId(long patientTrackId);
}
