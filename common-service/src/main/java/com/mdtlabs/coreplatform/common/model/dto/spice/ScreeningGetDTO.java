package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The ScreeningGetDTO class is a data transfer object that contains information about whether
 * assessment data is required, the patient track ID, and the screening ID.
 * </p>
 */
@Data
public class ScreeningGetDTO {

    private boolean isAssessmentDataRequired;

    private Long patientTrackId;

    private Long screeningId;
}
