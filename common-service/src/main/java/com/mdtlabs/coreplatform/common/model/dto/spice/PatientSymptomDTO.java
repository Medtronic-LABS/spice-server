package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The PatientSymptomDTO class contains data related to a patient's symptom, including an ID, symptom
 * ID, name, type, and other symptom information.
 * </p>
 */
@Data
public class PatientSymptomDTO {

    private Long id;

    private Long symptomId;

    private String name;

    private String type;

    private String otherSymptom;
}
