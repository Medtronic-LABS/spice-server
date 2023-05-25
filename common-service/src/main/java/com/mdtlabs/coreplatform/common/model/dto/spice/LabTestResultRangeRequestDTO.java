package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * This is a Java class representing a request for lab test result ranges, including a lab test result
 * ID and a list of lab test result range DTOs.
 * </p>
 */
@Data
@Validated
public class LabTestResultRangeRequestDTO extends CommonRequestDTO {

    private static final long serialVersionUID = 5972366800870662104L;

    private Long labTestResultId;

    /**
     * <p>
     * It is used to validate the list of `LabTestResultRangeDTO` objects in the
     * `labTestResultRanges` field of the `LabTestResultRangeRequestDTO` class
     * </p>
     */
    @Valid
    private List<LabTestResultRangeDTO> labTestResultRanges;

}
