package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO class for current medication entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class CurrentMedicationListDTO extends MedicalComplianceListDTO {
    private String type;
}
