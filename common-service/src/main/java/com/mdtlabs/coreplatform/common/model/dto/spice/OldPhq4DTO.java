package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * The OldPhq4DTO class represents a DTO (Data Transfer Object) for PHQ-4 (Patient Health
 * Questionnaire-4) assessment, including risk level, score, and a list of mental health information.
 * </p>
 */
@Data
public class OldPhq4DTO {

    private String phq4_risk_level;

    private int phq4_score;

    private List<OldMentalHealthDTO> phq4_mental_health;

}
