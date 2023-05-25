package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * This is a Java class representing a lab test result with its associated ranges and other properties.
 * </p>
 */
@Data
public class LabTestResultDTO {

    private Long id;

    private String name;

    private Long labTestId;

    private List<LabTestResultRangeDTO> labTestResultRanges;

    private int displayOrder;

    private Long tenantId;

}
