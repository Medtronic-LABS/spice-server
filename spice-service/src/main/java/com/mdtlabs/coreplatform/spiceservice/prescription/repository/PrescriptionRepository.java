package com.mdtlabs.coreplatform.spiceservice.prescription.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * PrescriptionRepository is a Java interface that extends the JpaRepository interface and defines methods for
 * accessing and manipulating data in the Prescription table of a database.
 * </p>
 *
 * @author Jeyaharini T A created on Fen 10, 2023
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    public static final String GET_PRESCRIPTIONS = "FROM Prescription WHERE id IN (:ids)";

    public static final String GET_ACTIVE_PRESCRIPTIONS = "FROM Prescription WHERE id IN (:ids) "
            + "AND isDeleted = :isDeleted";

    public static final String GET_REFILL_PRESCRIPTIONS = "FROM Prescription pres WHERE "
            + "pres.patientTrackId = :patientTrackId AND pres.remainingPrescriptionDays >"
            + " :remainingPrescriptionDays AND isDeleted = :isDeleted";

    public static final String PRESCRIPTION_COUNT = "Select count(id) from Prescription"
            + " as prescription where prescription.isDeleted=false and prescription.endDate"
            + " <= :endDate and prescription.patientTrackId = :patientTrackId ";

    /**
     * <p>
     * This method is used to retrieve a list of prescriptions based on a set of IDs.
     * </p>
     *
     * @param ids {@link Set<Long>} The ids represents the IDs of the prescriptions that need to be retrieved from
     *            the database is given
     * @return {@link List<Prescription>} The list of Prescription objects that match the
     * given ids is retrieved from the database
     */
    @Query(value = GET_PRESCRIPTIONS)
    public List<Prescription> getPrescriptions(@Param(Constants.IDS) Set<Long> ids);

    /**
     * <p>
     * This method is used to get the prescriptions by patient track id and deleted status.
     * </p>
     *
     * @param patientTrackId The patient track ID is used to identify a specific patient's medical history
     *                       and prescriptions is given
     * @param isDeleted      The boolean value that is used to filter the results of the query based on
     *                       whether the list of prescription has been marked as deleted or not is given
     * @return {@link List<Prescription>} The list of Prescription objects that match the given
     * patientTrackId, and isDeleted flag is retrieved from the database
     */
    public List<Prescription> findByPatientTrackIdAndIsDeleted(long patientTrackId, boolean isDeleted);

    /**
     * <p>
     * This method is used to find prescriptions by patient track ID, patient visit ID, and whether they are marked
     * as deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} The patient track ID is used to identify a specific patient's medical history
     *                       and prescriptions is given
     * @param patientVisitId {@link Long}The ID of the patient visit for which the prescription is being searched
     *                       is given
     * @param isDeleted      The boolean value that is used to filter the results of the query based on
     *                       whether the list of prescription has been marked as deleted or not is given
     * @return {@link List<Prescription>} The list of Prescription objects that match the given
     * patientTrackId, patientVisitId, and isDeleted flag is retrieved from the database
     */
    public List<Prescription> findByPatientTrackIdAndPatientVisitIdAndIsDeleted(Long patientTrackId,
                                                                                Long patientVisitId, boolean isDeleted);

    /**
     * <p>
     * This method is used to retrieve the prescription count for a given patient and end date.
     * </p>
     *
     * @param endDate        {@link Date} The endDate represents the end date for which the prescription count
     *                       is being queried is given
     * @param patientTrackId {@link Long} The ID of the patient's medical record or history that is being searched for
     *                       in the database is given
     * @return The integer value which represents the count of prescriptions for a given patient
     * track ID and end date is retrieved from the database
     */
    @Query(value = PRESCRIPTION_COUNT)
    public int getPrecriptionCount(@Param(Constants.END_DATE) Date endDate, @Param(Constants.PATIENT_TRACK_ID) Long patientTrackId);

    /**
     * <p>
     * This method is used to retrieve a list of prescription objects for a given patient track ID with remaining
     * prescription days and a flag indicating if the prescription has been deleted.
     * </p>
     *
     * @param patientTrackId            {@link Long} The ID of the patient's whose refill prescriptions that is being
     *                                  searched for in the database is given
     * @param remainingPrescriptionDays The remainingPrescriptionDays represents the number of days remaining for
     *                                  a prescription to expire is given
     * @param isDeleted                 The boolean value that is used to filter the results of the query based on
     *                                  whether the list of prescription has been marked as deleted or not is given
     * @return {@link List<Prescription>} The list of Prescription for the given patient track ID,
     * remainingPrescriptionDays and isDeleted flag is retrieved from the database
     */
    @Query(value = GET_REFILL_PRESCRIPTIONS)
    public List<Prescription> getRefillPrescriptions(@Param(Constants.PATIENT_TRACK_ID) Long patientTrackId,
                                                     @Param(Constants.REMAINING_PRESCRIPTION_DAYS) int remainingPrescriptionDays,
                                                     @Param(Constants.IS_DELETED) boolean isDeleted);

    /**
     * <p>
     * This method is used to retrieve a list of active prescriptions based on a list of IDs and a boolean flag
     * indicating if they have been deleted.
     * </p>
     *
     * @param ids       {@link List<Long>} The list of ID represent the IDs of the prescriptions that need to be
     *                  retrieved is given
     * @param isDeleted {@link Boolean} The boolean value that is used to filter the results of the query based on
     *                  whether the list of prescription has been marked as deleted or not is given
     * @return {@link List<Prescription>} The list of active prescriptions for the given list of IDs and isDeleted flag
     * is retrieved from the database
     */
    @Query(value = GET_ACTIVE_PRESCRIPTIONS)
    List<Prescription> getActivePrescriptions(@Param(Constants.IDS) List<Long> ids, @Param(Constants.IS_DELETED) Boolean isDeleted);

    /**
     * <p>
     * This method is used to find prescriptions for a given patient track ID with remaining prescription days greater
     * than a specified value and that have not been deleted.
     * </p>
     *
     * @param patientTrackId            The ID of the patient's medical record or history that is being searched for
     *                                  in the database is given
     * @param remainingPrescriptionDays The remainingPrescriptionDays represents the number of days remaining for
     *                                  a prescription to expire is given
     * @return {@link List<Prescription>} The list of Prescription for the given patient track ID and
     * remainingPrescriptionDays is retrieved from the database
     */
    public List<Prescription> findByPatientTrackIdAndRemainingPrescriptionDaysGreaterThanAndIsDeletedFalse(
            long patientTrackId, int remainingPrescriptionDays);

    /**
     * <p>
     * This method is used to find the most recently updated prescription for a specific patient and tenant.
     * </p>
     *
     * @param patientTrackId The ID of the patient's medical record or history that is being searched for
     *                       in the database is given
     * @param tenantId       {@link Long} The tenant ID for which the prescription is being searched is given
     * @return {@link Prescription} The prescription for the given patient Track ID and tenant ID is retrieved from
     * the database
     */
    public Prescription findFirstByPatientTrackIdAndTenantIdOrderByUpdatedByDesc(
            long patientTrackId, Long tenantId);

    /**
     * <p>
     * This method is used to find prescriptions by patient track ID.
     * </p>
     *
     * @param patientTrackId The patientTrackId is used to search for all the prescriptions associated with a
     *                       particular patient's track ID is given
     * @return {@link List<Prescription>} The List of Prescription for the given patientTrack ID is returned
     */
    public List<Prescription> findBypatientTrackId(long patientTrackId);
}
