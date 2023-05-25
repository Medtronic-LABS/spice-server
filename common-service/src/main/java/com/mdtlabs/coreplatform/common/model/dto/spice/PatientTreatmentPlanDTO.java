package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import lombok.Data;

/**
 * <p>
 * This class is a DTO for PatientTracker entity.
 * </p>
 *
 * @author Karthick M created on Feb 07, 2023
 */
@Data
public class PatientTreatmentPlanDTO {

    private Long id;

    private Long tenantId;

    private Long createdBy = getUserValue();

    private String medicalReviewFrequency;

    private String bpCheckFrequency;

    private String bgCheckFrequency;

    private String hba1cCheckFrequency;

    private Boolean isProvisional = Constants.BOOLEAN_FALSE;

    private String riskLevel;

    private Long patientVisitId;

    private Long patientTrackId;

    /**
     * <p>
     * This method is used to get user value
     * </p>
     *
     * @return String - user value
     */
    @JsonIgnore
    public long getUserValue() {
        if (null != UserContextHolder.getUserDto()) {
            return UserContextHolder.getUserDto().getId();
        }
        return Constants.ZERO;
    }
}
