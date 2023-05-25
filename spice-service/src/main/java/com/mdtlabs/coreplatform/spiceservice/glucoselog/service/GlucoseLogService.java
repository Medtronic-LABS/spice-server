package com.mdtlabs.coreplatform.spiceservice.glucoselog.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;

/**
 * <p>
 * This is an interface to perform any actions in glucose log related entities.
 * </p>
 *
 * @author Karthick Murugesan
 * @since Jun 30, 2022
 */
public interface GlucoseLogService {

    /**
     * <p>
     * This method adds a new Glucose log.
     * </p>
     *
     * @param glucoseLog             {@link GlucoseLog} entity
     * @param isPatientTrackerUpdate {@link boolean} true or false
     * @return GlucoseLog             {@link GlucoseLog} entity
     */
    GlucoseLog addGlucoseLog(GlucoseLog glucoseLog, boolean isPatientTrackerUpdate);

    /**
     * <p>
     * This method fetches a single glucose log.
     * </p>
     *
     * @param glucoseLogId {@link long} glucose log id
     * @return GlucoseLog  {@link GlucoseLog} entity
     */
    GlucoseLog getGlucoseLogById(long glucoseLogId);

    /**
     * <p>
     * This method fetches a single glucose log by patient track id.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @return GlucoseLog    {@link GlucoseLog} entity
     */
    GlucoseLog getGlucoseLogByPatientTrackId(long patientTrackId);

    /**
     * <p>
     * Gets glucose log by patient track id.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @param isLatest       {@link boolean} true or false
     * @return GlucoseLog {@link GlucoseLog} entity
     */
    GlucoseLog getGlucoseLogByPatientTrackIdAndIsLatest(long patientTrackId, boolean isLatest);

    /**
     * <p>
     * Gets patient glucose log dto with symptoms.
     * </p>
     *
     * @param requestData {@link RequestDTO} request dto
     * @return PatientGlucoseLogDto {@link PatientGlucoseLogDTO} patient glucose log dto
     */
    PatientGlucoseLogDTO getPatientGlucoseLogsWithSymptoms(RequestDTO requestData);

    /**
     * <p>
     * Gets the glucose log with patient track id and is deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param booleanFalse   {@link Boolean} is deleted
     * @return GlucoseLog {@link GlucoseLog} entity
     */
    GlucoseLog findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(Long patientTrackId,
                                                                         Boolean booleanFalse);

    /**
     * <p>
     * The function removes a glucose log associated with a specific tracker ID.
     * </p>
     *
     * @param trackerId {@link long} The trackerId parameter is a unique identifier for a glucose log that needs to
     *                  be removed from a system or database.
     */
    void removeGlucoseLog(long trackerId);

}
