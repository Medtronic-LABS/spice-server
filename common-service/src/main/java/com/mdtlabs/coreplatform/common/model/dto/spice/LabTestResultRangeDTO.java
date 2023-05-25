package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * This is a Java class representing a data transfer object for lab test result ranges, containing
 * various properties such as ID, test ID, minimum and maximum values, unit, display order, and display
 * name.
 * </p>
 */
@Data
@Validated
public class LabTestResultRangeDTO {
    private Long id;

    private Long labTestId;

    private Double minimumValue;

    private Double maximumValue;

    private String unit;

    private Long unitId;

    private Integer displayOrder;

    private String displayName;
}
