package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * EnrollmentRequestDTO to handle request for Enrollment.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Data
public class EnrollmentRequestDTO {

    private Boolean isPregnant;

    @NotEmpty(message = ErrorConstants.GENDER_NOT_NULL)
    private String gender;

    private Integer cvdRiskScore;

    private Date dateOfBirth;

    private String unitMeasurement;

    private String cvdRiskScoreDisplay;

    private BioMetricsDTO bioMetrics;

    private String cvdRiskLevel;

    @NotNull(message = ErrorConstants.SITE_ID_NOT_NULL)
    private Long siteId;

    private GlucoseLog glucoseLog;

    @Valid
    private BioDataDTO bioData;

    private Long patientTrackId;

    @NotNull(message = ErrorConstants.AGE_NOT_NULL)
    @PositiveOrZero(message = ErrorConstants.AGE_MIN_VALUE)
    private Integer age;

    @NotNull(message = ErrorConstants.IS_REGULAR_SMOKER)
    private Boolean isRegularSmoker;

    private MentalHealth phq4;

    private Long tenantId;

    private List<String> provisionalDiagnosis;

    private List<Map<String, Object>> customizedWorkflows;

    @NotNull
    @Valid
    private BpLog bpLog;

    private DiagnosisDTO patientStatus;

    private Long programId;
}
