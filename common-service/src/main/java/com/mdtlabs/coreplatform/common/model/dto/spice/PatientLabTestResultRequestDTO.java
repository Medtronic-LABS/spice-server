package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This class is a DTO used to get request for patient lab test result entity operations.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Data
public class PatientLabTestResultRequestDTO {

    private Long patientLabTestId;

    private Long tenantId;

    private Boolean isReviewed;

    private Date testedOn;

    private String comment;

    private Boolean isEmptyRanges;

    @Valid
    private List<PatientLabTestResultDTO> patientLabTestResults;

    private Boolean isAbnormal = false;

    private String roleName;

    private Long patientVisitId;
}
