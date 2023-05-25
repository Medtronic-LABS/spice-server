package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class is an DTO for diagnosis fields.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Data
public class DiagnosisDTO {

    private Integer htnYearOfDiagnosis;

    private Integer diabetesYearOfDiagnosis;

    private String htnPatientType;

    private String diabetesPatientType;

    private String diabetesDiagnosis;

    private String diabetesDiagControlledType;

    private Boolean isHtnDiagnosis;

    private Boolean isDiabetesDiagnosis;

    private String htnDiagnosis;

    private List<String> confirmDiagnosis = new ArrayList<>();

    private String diagnosisDiabetes;

    private String diagnosisHypertension;

}
