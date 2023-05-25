package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The PatientComplianceDTO class contains data related to a patient's compliance with a certain
 * treatment or medication.
 * </p>
 */
@Data
public class PatientComplianceDTO {

    private Long id;

    private String name;

    private Long complianceId;

    private String otherCompliance;
}
