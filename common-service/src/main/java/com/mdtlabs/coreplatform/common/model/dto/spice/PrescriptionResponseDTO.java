package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is DTO class for prescription response
 * </p>
 */
@Data
public class PrescriptionResponseDTO {

    private Long id;

    private String signature;

    private String medicationName;

    private String dosageUnitValue;

    private String dosageUnitName;

    private String dosageFrequencyName;

    private Integer prescribedDays;

    private String instructionNote;

    private String dosageFormName;

}