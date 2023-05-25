package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class is an Request DTO class for assessment.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
@Validated
public class AssessmentDTO extends CommonRequestDTO {

    private String nationalId;

    @Valid
    private BpLog bpLog;

    private GlucoseLog glucoseLog;

    private String gender;

    private String firstName;

    private String lastName;

    private Integer age;

    @NotNull(message = ErrorConstants.IS_REGULAR_SMOKER)
    private Boolean isRegularSmoker;

    private Integer cvdRiskScore;

    private String unitMeasurement;

    private String cvdRiskScoreDisplay;

    private String cvdRiskLevel;

    private MentalHealth phq4;

    @Valid
    private List<ComplianceDTO> compliances;

    @Valid
    private List<SymptomDTO> symptoms;

    private Long siteId;

    private List<Map<String, Object>> customizedWorkflows;

    private List<String> provisionalDiagnosis;

}
