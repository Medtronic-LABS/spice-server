package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class is a data transfer objects for enrollment response.
 * </p>
 *
 * @author Niraimathi S created on Feb 7, 2023
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnrollmentResponseDTO {

    private String phoneNumber;

    private PatientDetailDTO enrollment;

    private GlucoseLogDTO glucoseLog;

    private BpLogDTO bpLog;

    private List<Map<String, String>> treatmentPlan;

    private Boolean isConfirmDiagnosis;

    private List<String> confirmDiagnosis;

    private List<String> provisionalDiagnosis;

    private MentalHealthDTO phq4;
}
