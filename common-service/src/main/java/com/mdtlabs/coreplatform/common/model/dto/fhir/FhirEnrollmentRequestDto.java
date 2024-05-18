package com.mdtlabs.coreplatform.common.model.dto.fhir;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 *   This class is an Request DTO class for FhirEnrollmentRequest.
 * </p>
 *
 * @author Yogeshwaran M created on 17 Nov 2023
 */
@Data
public class FhirEnrollmentRequestDto {

    @NotNull(message = Constants.TYPE_NOT_NULL_MESSAGE)
    private String type;

    private Long patientTrackId;

    private Patient patient;

    private BpLog bpLog;

    private GlucoseLog glucoseLog;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

}
