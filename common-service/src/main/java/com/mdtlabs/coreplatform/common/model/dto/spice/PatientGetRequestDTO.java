package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO for patient entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Data
public class PatientGetRequestDTO {
    private Long id;

    private boolean assessmentRequired;

    private boolean isGad7;

    private boolean isLifeStyleRequired;

    private boolean isPhq9;

    private boolean isPregnant;

    private boolean isPrescriberRequired;

    private boolean isRedRiskPatient;
}
