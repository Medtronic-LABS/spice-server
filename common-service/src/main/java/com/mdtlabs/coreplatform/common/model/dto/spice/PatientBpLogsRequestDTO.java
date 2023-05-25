package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The class PatientBpLogsRequestDTO contains two fields, isLatestRequired and patientTrackerId, used
 * for requesting blood pressure logs for a patient.
 * </p>
 */
@Data
public class PatientBpLogsRequestDTO {
    private boolean isLatestRequired;

    private long patientTrackerId;

}
