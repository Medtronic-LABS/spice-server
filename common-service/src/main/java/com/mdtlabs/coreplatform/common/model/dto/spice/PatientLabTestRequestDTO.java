package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * This DTO class handles the request for patient lab test.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
public class PatientLabTestRequestDTO {

    private Long tenantId;

    private Long patientVisitId;

    @NotNull(message = ErrorConstants.PATIENT_TRACK_ID_NOT_NULL)
    private Long patientTrackId;

    @Valid
    private List<PatientLabTest> labTest;

}