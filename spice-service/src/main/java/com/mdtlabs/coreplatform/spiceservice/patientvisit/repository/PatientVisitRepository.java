package com.mdtlabs.coreplatform.spiceservice.patientvisit.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * PatientVisitRepository is a Java interface that extends the JpaRepository interface and defines methods for
 * accessing and manipulating data in the PatientVisit table of a database.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface PatientVisitRepository extends JpaRepository<PatientVisit, Long> {

    String GET_PATIENT_VISIT_BY_TRACK_ID = "SELECT * from patient_visit "
            + " where patient_track_id=:patientTrackId and is_deleted=false and visit_date between"
            + " CAST(:startDate AS TIMESTAMP) and CAST(:endDate AS TIMESTAMP)";

    String GET_PATIENT_VISIT_DATES = "SELECT visit from PatientVisit as visit "
            + " WHERE visit.patientTrackId =:patientTrackId"
            + " AND (:isInvestigation is null or visit.isInvestigation=:isInvestigation)"
            + " AND (:isMedicalReview is null or visit.isMedicalReview=:isMedicalReview) "
            + " AND (:isPrescription is null or visit.isPrescription=:isPrescription)"
            + " AND visit.isDeleted=false ORDER BY visit.visitDate";

    String GET_ALL_PATIENT_VISIT_DATES = "SELECT visit from PatientVisit as visit "
            + " WHERE visit.patientTrackId =:patientTrackId AND visit.isDeleted=false"
            + " AND (visit.isInvestigation=true OR visit.isMedicalReview=true OR visit.isPrescription=true)"
            + " ORDER BY visit.visitDate";

    /**
     * <p>
     * This method is used to retrieve a patient visit by track ID, start date, and end date using a native SQL
     * query.
     * </p>
     *
     * @param patientTrackId {@link Long} The patient track ID is used to retrieve the visit dates of a specific
     *                       patient is retrieved from the database is given
     * @param startDate      {@link String} The start date represents the beginning date of the time range for which
     *                       the patient visit data is being queried is given
     * @param endDate        {@link String} The endDate represents the end date of the patient visit is given
     * @return {@link PatientVisit} The patient visit for the given patient track ID, start date and end date is
     * retrieved from the database
     */
    @Query(value = GET_PATIENT_VISIT_BY_TRACK_ID, nativeQuery = true)
    PatientVisit getPatientVisitByTrackId(@Param(Constants.PATIENT_TRACK_ID) Long patientTrackId,
                                          @Param(Constants.START_DATE) String startDate, @Param(Constants.END_DATE) String endDate);

    /**
     * <p>
     * This method is used to retrieve a list of patient visit dates based on certain parameters.
     * </p>
     *
     * @param patientTrackId  {@link Boolean} The patient track ID is used to retrieve the visit dates of a specific
     *                        patient is retrieved from the database is given
     * @param isInvestigation {@link Boolean} The boolean value that indicates whether the query should return patient
     *                        visits that have an investigation associated with them is given
     * @param isMedicalReview {@link Boolean} The Boolean value that is used as a filter to retrieve patient visit
     *                        dates based on whether they have a medical review or not is given
     * @param isPrescription  {@link Boolean} The Boolean value that is used as a filter to retrieve patient visits that
     *                        have prescription records associated with them is given
     * @return {@link List<PatientVisit>} The list of PatientVisit for the given value is retrieved from the database
     */
    @Query(value = GET_PATIENT_VISIT_DATES)
    List<PatientVisit> getPatientVisitDates(@Param(Constants.PATIENT_TRACK_ID) Long patientTrackId,
                                            @Param(Constants.IS_INVESTIGATION) Boolean isInvestigation, @Param(Constants.IS_MEDICAL_REVIEW) Boolean isMedicalReview,
                                            @Param(Constants.IS_PRESCRIPTION) Boolean isPrescription);

    /**
     * This method is used to find a patient visit by its ID and checks if it has been deleted.
     *
     * @param id        {@link Long} The id is used to retrieve a patient visit from the database is given
     * @param isDeleted The boolean value that is used to filter the results of the query based on
     *                  whether patient visit has been marked as deleted or not is given
     * @return {@link PatientVisit} The patient visit for the given id and isDeleted flag is retrieved from the
     * database
     */
    PatientVisit findByIdAndIsDeleted(Long id, boolean isDeleted);

    /**
     * <p>
     * This method is used to find a patient visit by its ID and tenant ID.
     * </p>
     *
     * @param id       {@link Long} The id is used to retrieve a patient visit from the database is given
     * @param tenantId {@link Long} The tenant ID for which the patient visit  is being searched is given
     * @return {@link PatientVisit} The patient visit for the given ID and tenant ID is retrieved from the database
     */
    PatientVisit findByIdAndTenantId(Long id, Long tenantId);

    /**
     * <p>
     * This method is used to retrieve a list of visit dates for a given patient track ID.
     * </p>
     *
     * @param patientTrackId {@link Long} The patientTrackId is used to retrieve all the visit dates of a
     *                       particular patient is given
     * @return {@link List<PatientVisit>} The list of PatientVisit for the given patient track ID
     * is retrieved from the database
     */
    @Query(value = GET_ALL_PATIENT_VISIT_DATES)
    List<PatientVisit> getPatientVisitDates(@Param(Constants.PATIENT_TRACK_ID) Long patientTrackId);

    /**
     * <p>
     * This method is used to find all patient visits associated with a given patient track ID.
     * </p>
     *
     * @param patientTrackId The patient track is used to track all visits and treatments associated
     *                       with that patient is given
     * @return {@link List<PatientVisit>} The list of PatientVisit that match the given patientTrackId
     * is retrieved from the database
     */
    List<PatientVisit> findByPatientTrackId(long patientTrackId);

    /**
     * <p>
     * This method is used to find patient visits by their track ID and whether they have been marked as deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} The patient track is used to track all visits and treatments associated
     *                       with that patient is given
     * @param isDeleted      {@link Boolean} The boolean value that is used to filter the results of the query based on
     *                       whether the list of patient visit has been marked as deleted or not is given
     * @return {@link List<PatientVisit>} This list of PatientVisit for the given patientTrackId and isDeleted
     * flag is retrieved from the database
     */
    List<PatientVisit> findByPatientTrackIdAndIsDeleted(Long patientTrackId,
                                                        Boolean isDeleted);
}
