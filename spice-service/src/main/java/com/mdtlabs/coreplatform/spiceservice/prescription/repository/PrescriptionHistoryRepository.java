package com.mdtlabs.coreplatform.spiceservice.prescription.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * PrescriptionHistoryRepository is a Java interface that extends the JpaRepository interface and defines methods for
 * accessing and manipulating data in the Prescription history table of a database.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface PrescriptionHistoryRepository extends JpaRepository<PrescriptionHistory, Long> {

    String GET_PRESCRIPTIONS = "select * from (select *, rank() over (partition by prescription_id order by created_at desc) from prescription_history where patient_visit_id=:patientVisitId) as a where a.rank = 1";

    String GET_PRESCRIPTION_HISTORY = "SELECT * FROM (SELECT *, row_number() "
            + "OVER (PARTITION BY prescription_id,"
            + " patient_visit_id ORDER BY created_at DESC) AS row_number FROM prescription_history "
            + " WHERE (:prescriptionId IS null OR prescription_id = :prescriptionId )"
            + " AND (:patientVisitId IS null OR patient_visit_id = :patientVisitId)"
            + " AND (:patientTrackId IS null OR patient_track_id = :patientTrackId) AND last_refill_date IS null ) prescriptionhistories"
            + " WHERE row_number = 1 ORDER BY created_at ASC";

    String GET_FILL_PRESCRIPTION_HISTORY = "FROM PrescriptionHistory"
            + " WHERE patientTrackId = :patientTrackId AND patientVisitId = :patientVisitId"
            + " AND prescriptionFilledDays != :prescriptionFilledDays"
            + " AND lastRefillDate IS NOT null ORDER BY createdAt DESC ";

    /**
     * <p>
     * This method is used to retrieve the prescription history for a patient within a specified number of days.
     * </p>
     *
     * @param patientTrackId         {@link Long} The ID of the patient's medical record or history that is being
     *                               searched for in the database is given
     * @param patientVisitId         {@link Long} The ID of the patient visit for which the prescription is being
     *                               searched is given
     * @param prescriptionFilledDays The prescriptionFilledDays is an integer value that represents the number of days
     *                               for which the prescription has been filled is given
     * @return {@link List<PrescriptionHistory>} The list of PrescriptionHistory that matches the given patient track ID
     * patient visit ID and prescriptionFilledDays is retrieved from the database
     */
    @Query(value = GET_FILL_PRESCRIPTION_HISTORY)
    List<PrescriptionHistory> getFillPrescriptionHistory(@Param(Constants.PATIENT_TRACK_ID) Long patientTrackId,
                                                         @Param(Constants.PATIENT_VISIT_ID) Long patientVisitId, @Param(Constants.PRESCRIPTION_FILLED_DAYS) int prescriptionFilledDays);

    /**
     * <p>
     * This method is used to retrieve prescription history for a given prescription ID, patient visit ID, and
     * patient track ID.
     * </p>
     *
     * @param prescriptionId {@link Long}The ID of the prescription is being searched
     *                       is given
     * @param patientVisitId {@link Long} The ID of the patient visit for which the prescription is being searched
     *                       is given
     * @param patientTrackId {@link Long} The ID of the patient's medical record or history that is being searched for
     *                       in the database is given
     * @return {@link List<PrescriptionHistory>} The list of PrescriptionHistory that matches the given prescription,
     * patient visit ID and patient track ID is retrieved from the database
     */
    @Query(value = GET_PRESCRIPTION_HISTORY, nativeQuery = true)
    List<PrescriptionHistory> getPrescriptionHistory(@Param(Constants.PRESCRIPTION_ID) Long prescriptionId,
                                                     @Param(Constants.PATIENT_VISIT_ID) Long patientVisitId, @Param(Constants.PATIENT_TRACK_ID) Long patientTrackId);

    /**
     * <p>
     * This method is used to retrieve a list of prescription history for a given patient visit ID using
     * a native SQL query.
     * </p>
     *
     * @param patientVisitId The ID of the patient visit for which the prescription is being searched
     *                       is given
     * @return {@link List<PrescriptionHistory>} The list of PrescriptionHistory that matches the given patient visit
     * ID is retrieved from the database
     */
    @Query(value = GET_PRESCRIPTIONS, nativeQuery = true)
    List<PrescriptionHistory> getPrescriptions(@Param(Constants.PATIENT_VISIT_ID) Long patientVisitId);

    /**
     * <p>
     * This method is used to find the first prescription history for a given patient track ID where the prescription
     * filled days are greater than a specified value, ordered by the updated date in descending order.
     * </p>
     *
     * @param patientTrackId         The ID of the patient's medical record or history that is being searched for
     *                               in the database is given
     * @param prescriptionFilledDays The prescriptionFilledDays is an integer value that represents the number of days
     *                               for which the prescription has been filled is given
     * @return {@link PrescriptionHistory} The first PrescriptionHistory object that matches the given patientTrackId
     * and has a prescriptionFilledDays greater than the given value, sorted in descending order by the updatedAt field
     * is retrieved from the database
     */
    PrescriptionHistory findFirstByPatientTrackIdAndPrescriptionFilledDaysGreaterThanOrderByUpdatedAtDesc(
            long patientTrackId, int prescriptionFilledDays);

    /**
     * <p>
     * This method is used to finds prescription history records by patient track ID and whether they have been marked
     * as deleted.
     * </p>
     *
     * @param patientTrackId The ID of the patient's medical record or history that is being searched for
     *                       in the database is given
     * @return {@link List<PrescriptionHistory>} The list of PrescriptionHistory objects that match the given
     * patientTrackId is retrieved from the database
     */
    List<PrescriptionHistory> findByPatientTrackIdAndIsDeleted(long patientTrackId, boolean isDeleted);

    /**
     * <p>
     * This method is used to find prescription history records by patient track ID.
     * </p>
     *
     * @param patientTrackId The ID of the patient's medical record or history that is being searched for
     *                       in the database is given
     * @return {@link List<PrescriptionHistory>} The list of PrescriptionHistory objects that match the given
     * patientTrackId is retrieved from the database
     */
    List<PrescriptionHistory> findByPatientTrackId(long patientTrackId);
}
