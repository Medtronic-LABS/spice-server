package com.mdtlabs.coreplatform.spiceservice.patienttransfer.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTransfer;
import com.mdtlabs.coreplatform.common.model.enumeration.spice.PatientTransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientTransferRepository extends JpaRepository<PatientTransfer, Long> {

    String GET_PATIENT_TRANSFER = "select patientTransfer from PatientTransfer as patientTransfer ";

    String GET_PATIENT_TRANSFER_COUNT = "select count(patientTransfer.id) from PatientTransfer as patientTransfer "
            + "where patientTransfer.isDeleted = false and patientTransfer.isShow = true and ((patientTransfer.oldSite.id = :siteId and patientTransfer.transferBy.id = :userId ) or (patientTransfer.transferTo.id = :userId and patientTransfer.transferStatus = 'PENDING' and patientTransfer.transferSite.id = :siteId))";

    String GET_PATIENT_TRANSFER_BY_PATIENT_TRACKER_AND_STATUS = GET_PATIENT_TRANSFER
            + "where patientTransfer.isDeleted = false and patientTransfer.transferStatus = :transferStatus and patientTransfer.patientTracker.id = :patientTrackerId";

    String GET_PATIENT_TRANSFER_BY_PATIENT_TRACKER = GET_PATIENT_TRANSFER
            + "where patientTransfer.patientTracker.id = :patientTrackerId";

    String GET_INCOMING_LIST = GET_PATIENT_TRANSFER
            + "where patientTransfer.isDeleted = false and patientTransfer.transferSite.id = :siteId and patientTransfer.transferTo.id = :transferTo and patientTransfer.transferStatus = :transferStatus order by patientTransfer.updatedAt desc";

    String GET_OUTGOING_LIST = GET_PATIENT_TRANSFER
            + "where patientTransfer.isDeleted = false and patientTransfer.oldSite.id = :siteId and patientTransfer.transferBy.id = :transferBy and patientTransfer.isShow = true order by patientTransfer.updatedAt desc";

    PatientTransfer findByIdAndIsDeleted(long id, boolean isDeleted);

    /**
     * <p>
     * This Java function queries and returns a PatientTransfer object based on a patient tracker ID and
     * transfer status.
     * </p>
     *
     * @param patientTrackerId {@link Long} It is a Long type parameter that represents the unique identifier of a
     *                         patient tracker is given
     * @param transferStatus   {@link PatientTransferStatus} transferStatus is a parameter of type PatientTransferStatus which is used
     *                         to filter the patient transfer records based on their status is given
     * @return {@link PatientTransfer} The method is returning a single object of type `PatientTransfer` that matches the given
     * `patientTrackerId` and `transferStatus` is returned
     */
    @Query(value = GET_PATIENT_TRANSFER_BY_PATIENT_TRACKER_AND_STATUS)
    PatientTransfer findByPatientTrackIdAndTransferStatus(@Param("patientTrackerId") Long patientTrackerId,
                                                          @Param("transferStatus") PatientTransferStatus transferStatus);

    /**
     * <p>
     * This function retrieves the count of patient transfers for a specific site and user.
     * </p>
     *
     * @param siteId {@link Long} The siteId parameter is a Long type variable that represents the unique identifier
     *               of a site in the system is given
     * @param userId {@link Long} The userId parameter is a Long type variable that represents the unique identifier
     *               of a user is given
     * @return {@link Long} A Long value representing the count of patient transfers for a specific site and user is returned
     */
    @Query(value = GET_PATIENT_TRANSFER_COUNT)
    Long getPatientTransferCount(@Param("siteId") Long siteId,
                                 @Param("userId") Long userId);

    /**
     * <p>
     * Get list of incoming patient list.
     * </p>
     *
     * @param siteId     {@link Long} site id is given
     * @param transferTo {@link Long} transferTo is given
     * @return {@link List<PatientTransfer>} PatientTransfer list is given
     */
    @Query(value = GET_INCOMING_LIST)
    List<PatientTransfer> getIncomingList(@Param("siteId") Long siteId,
                                          @Param("transferTo") Long transferTo, @Param("transferStatus") PatientTransferStatus transferStatus);

    /**
     * <p>
     * This Java function retrieves a list of patient transfers based on the site ID and transfer by
     * parameters.
     * </p>
     *
     * @param siteId     {@link Long} The ID of the site for which the outgoing patient transfers are being requested is given
     * @param transferBy {@link Long} The "transferBy" parameter is a Long type variable that represents the ID of 
     *                   the user who initiated the patient transfer is given
     * @return {@link List<PatientTransfer>} A list of PatientTransfer objects is being returned is returned
     */
    @Query(value = GET_OUTGOING_LIST)
    List<PatientTransfer> getOutgoingList(@Param("siteId") Long siteId,
                                          @Param("transferBy") Long transferBy);

    /**
     * <p>
     * This function retrieves a list of patient transfers based on a given patient tracker ID.
     * </p>
     *
     * @param patientTrackerId {@link Long} patientTrackerId is a Long type parameter that represents the ID of a
     *                         patient tracker is given
     * @return {@link List<PatientTransfer>} A list of PatientTransfer objects that match the given patientTrackerId parameter is returned
     */
    @Query(value = GET_PATIENT_TRANSFER_BY_PATIENT_TRACKER)
    List<PatientTransfer> findByPatientTrackId(@Param("patientTrackerId") Long patientTrackerId);
}