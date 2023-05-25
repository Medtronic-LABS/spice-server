package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Set;

/**
 * <p>
 * The RiskAlgorithmDTO class is a data transfer object that contains information about a patient's
 * glucose levels, blood pressure, pregnancy status, symptoms, and calculated risk level.
 * </p>
 */
@Data
public class RiskAlgorithmDTO {

    private Long patientTrackId;

    private String glucoseType;

    private Double glucoseValue;

    private Integer avgDiastolic;

    private Integer avgSystolic;

    private Boolean isPregnant;

    private String riskLevel;

    private Set<Long> symptoms;

}