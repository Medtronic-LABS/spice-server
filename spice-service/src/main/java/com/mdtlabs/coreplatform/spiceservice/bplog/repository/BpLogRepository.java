package com.mdtlabs.coreplatform.spiceservice.bplog.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the BpLog module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Repository
public interface BpLogRepository extends JpaRepository<BpLog, Long> {

    String GET_BP_LOG_BY_PATIENT_TRACK_ID = "select bplog from BpLog as bplog where "
            + "bplog.patientTrackId = :patientTrackId and isLatest=true and "
            + "bplog.createdAt > :today order by bplog.updatedAt";

    /**
     * Find BpLog by patientTracker and isDeleted and isLatest.
     *
     * @param patientTrackId {@link long } patient track id is given
     * @param isDeleted      {@link boolean} true or false is given
     * @param isLatest       {@link boolean} true or false is given
     * @return {@link BpLog} BpLog entity is returned
     */
    BpLog findByPatientTrackIdAndIsDeletedAndIsLatest(long patientTrackId, boolean isDeleted, boolean isLatest);

    /**
     * Find list of BpLog by patientTrackId and isCreated within 24 hours.
     *
     * @param patientTrackId {@link long} patient track id is given
     * @param date           {@link Date} date is given
     * @return {@link List<BpLog>} BpLog entity is returned
     */
    @Query(value = GET_BP_LOG_BY_PATIENT_TRACK_ID)
    List<BpLog> findByPatientTrackIdAndIsCreatedToday(@Param("patientTrackId") long patientTrackId,
                                                      @Param("today") Date date);

    /**
     * Get list of BpLogs by patient track id.
     *
     * @param patientTrackId {@link long} patient track id is given
     * @return {@link List<BpLog>} BpLog entity is returned
     */
    List<BpLog> findByPatientTrackId(long patientTrackId);


    /**
     * Gets BpLog based on patient track id.
     *
     * @param patientTrackId {@link long} patient track id is given
     * @param booleanFalse   {@link Boolean} false is given
     * @return {@link BpLog} - entity is returned
     */
    BpLog findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(long patientTrackId, Boolean booleanFalse);


    /**
     * This function finds all non-deleted BpLog entries associated with a given patient track ID and
     * returns them in a paginated format.
     *
     * @param patientTrackId {@link Long} The ID of the patient track for which the blood pressure logs are being
     *                       searched is given
     * @param pagination     {@link Pageable} pagination is an object of type Pageable which is used for pagination and
     *                       sorting of the results returned by the query is given
     * @return {@link Page<BpLog>} The method is returning a Page object containing a list of BpLog objects that match the
     * given patientTrackId and have not been marked as deleted, based on the provided pagination
     * parameters is returned
     */
    Page<BpLog> findByPatientTrackIdAndIsDeletedFalse(Long patientTrackId, Pageable pagination);


}
