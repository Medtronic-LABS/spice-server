package com.mdtlabs.coreplatform.spiceservice.glucoselog.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
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
 * database. This class used to perform all the GlucoseLog module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author Karthick Murugesan
 */
@Repository
public interface GlucoseLogRepository extends JpaRepository<GlucoseLog, Long> {

    String GET_GLUCOSE_LOG_BY_PATIENT_TRACK_ID =
            "select glucoselog from GlucoseLog as glucoselog where "
                    + "glucoselog.patientTrackId = :patientTrackId and isLatest=true and "
                    + "glucoselog.createdAt > :yesterday order by glucoselog.updatedAt desc";

    String GET_GLUCOSE_LOG_BY_PATIENT_TRACK_ID_AND_IS_LATEST =
            "select glucoselog from GlucoseLog as glucoselog where "
                    + "glucoselog.patientTrackId = :patientTrackId and isLatest=true";

    String GET_GLUCOSE_LOGS =
            "Select glucoselog from GlucoseLog as glucoselog where glucoselog.patientTrackId = :patientTrackId and "
                    + "isDeleted = false";
    String GET_GLUCOSE_LOG_COUNT = "Select count(*) from glucose_log where patient_track_id=:patientTrackId and " +
            "is_deleted=false";

    /**
     * <p>
     * This method fetches a single Glucose log by patient track id.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @return GlucoseLog {@link List<GlucoseLog>} Entity
     * @author Victor Jefferson
     */
    @Query(value = GET_GLUCOSE_LOG_BY_PATIENT_TRACK_ID)
    List<GlucoseLog> findByPatientTrackIdAndIsCreatedToday(@Param("patientTrackId") long patientTrackId,
                                                           @Param("yesterday") Date date);

    /**
     * <p>
     * Finds the patient tracker by id.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param isDeleted      {@link boolean} true or false
     * @param isLatest       {@link boolean} true or false
     * @return {@link GlucoseLog} GlucoseLog entity
     */
    GlucoseLog findByPatientTrackIdAndIsDeletedAndIsLatest(Long patientTrackId,
                                                           boolean isDeleted, boolean isLatest);


    /**
     * <p>
     * Updates the GlucoseLog latest status.
     * </p>
     *
     * @param patientTrackId {@link long} glucose log id
     * @return List {@link List<GlucoseLog>} GlucoseLog lists
     */
    @Query(value = GET_GLUCOSE_LOG_BY_PATIENT_TRACK_ID_AND_IS_LATEST)
    List<GlucoseLog> getGlucoseLogLatestStatus(@Param("patientTrackId") long patientTrackId);

    /**
     * <p>
     * Gets glucose logs by id.
     * </p>
     *
     * @param patientTrackId {@link Long} glucose log id
     * @param pageable       {@link Pageable} object
     * @return Page(GlucoseLog) {@link Page<GlucoseLog> } page of entity
     */
    @Query(value = GET_GLUCOSE_LOGS)
    Page<GlucoseLog> getGlucoseLogs(@Param("patientTrackId") Long patientTrackId, Pageable pageable);

    /**
     * <p>
     * Gets glucose log by patient track id.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param booleanFalse   {@link Boolean} false
     * @return GlucoseLog {@link GlucoseLog} entity
     */
    GlucoseLog findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(Long patientTrackId,
                                                                         Boolean booleanFalse);

    /**
     * <p>
     * Gets glucoselog count based on glucoselogId
     * </p>
     *
     * @param id {@link Long} PatientTrackId
     * @return {{@link Integer}} integer of total glucose cout
     */
    @Query(value = GET_GLUCOSE_LOG_COUNT, nativeQuery = true)
    Integer totalGlucoseLogCount(@Param("patientTrackId") Long id);


    /**
     * <p>
     * Get list of GlucoseLog by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @return {@link List<GlucoseLog>} GlucoseLog entity
     */
    List<GlucoseLog> findByPatientTrackId(long patientTrackId);
}
