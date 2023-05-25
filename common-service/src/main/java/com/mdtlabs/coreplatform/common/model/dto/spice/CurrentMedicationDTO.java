package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The CurrentMedicationDTO class represents a data transfer object for current medication information.
 * </p>
 */
@Data
public class CurrentMedicationDTO {

    private Long currentMedicationId;

    private String name;

    private String type;

    private String otherMedication;
}
