package com.mdtlabs.coreplatform.spiceservice.bplog.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.PatientBpLogsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;

/**
 * <p>
 * This is an interface to perform any actions in bpLog related entities.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
public interface BpLogService {

    /**
     * <p>
     * Creates new bp log information.
     * </p>
     *
     * @param bpLog                  {@link BpLog} entity is given
     * @param isPatientTrackerUpdate {@link boolean} true or false is given
     * @return {@link BpLog} entity is returned
     */
    BpLog addBpLog(BpLog bpLog, boolean isPatientTrackerUpdate);

    /**
     * <p>
     * Fetches the bp log value by patient track id.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id is given
     * @return {@link BpLog}  entity is given
     */
    BpLog getPatientTodayBpLog(long patientTrackId);

    /**
     * <p>
     * Gets bpLog value based on patientTrackId and isLatest fields.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id is given
     * @param isLatest       {@link boolean} true or false is given
     * @return {@link BPLog} - entity is returned
     */
    BpLog getBpLogByPatientTrackIdAndIsLatest(long patientTrackId, boolean isLatest);

    /**
     * <p>
     * Gets BPLog entity with symptoms.
     * </p>
     *
     * @param requestData {@link RequestDTO} Request data is given
     * @return {@link PatientBpLogsDTO} entity is returned
     */
    PatientBpLogsDTO getPatientBpLogsWithSymptoms(RequestDTO requestData);

    /**
     * <p>
     * Gets BpLog with patient track id and is deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id is given
     * @param booleanFalse   {@link Boolean} is deleted boolean is given
     * @return {@link BpLog} entity is returned
     */
    BpLog findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(Long patientTrackId, Boolean booleanFalse);

    void removeBpLog(long trackerId);

    BpLog getBpLogById(long bpLogId);

}
