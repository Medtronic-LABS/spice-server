package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Set;

/**
 * <p>
 * The CurrentMedicationDetailsDTO class contains information about a patient's current medications,
 * drug allergies, and adherence to medication.
 * </p>
 */
@Data
public class CurrentMedicationDetailsDTO {

    private Set<CurrentMedicationDTO> medications;

    private boolean isDrugAllergies;

    private boolean isAdheringCurrentMed;

    private String adheringMedComment;

    private String allergiesComment;
}
