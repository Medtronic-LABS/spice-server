package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * The `DiagnosisDetailsDTO` class is a Java class that contains fields and constructors for diagnosis
 * details, including provisional and confirmed diagnoses.
 * </p>
 */
@Data
public class DiagnosisDetailsDTO {

    private Boolean isConfirmDiagnosis;

    private List<String> provisionalDiagnosis;

    private List<String> confirmDiagnosis;


    /**
     * <p>
     * This is a constructor for the `DiagnosisDetailsDTO` class
     * </p>
     *
     * @param isConfirmDiagnosis   `isConfirmDiagnosis` of type `Boolean`
     * @param provisionalDiagnosis {@link List<String>} `provisionalDiagnosis` of type `List<String>`
     * @param confirmDiagnosis     {@link List<String>} `confirmDiagnosis` of type `List<String>`
     */
    public DiagnosisDetailsDTO(Boolean isConfirmDiagnosis, List<String> provisionalDiagnosis,
                               List<String> confirmDiagnosis) {
        this.isConfirmDiagnosis = isConfirmDiagnosis;
        this.provisionalDiagnosis = provisionalDiagnosis;
        this.confirmDiagnosis = confirmDiagnosis;
    }

    /**
     * <p>
     * The `public DiagnosisDetailsDTO() {}` is a default constructor for the `DiagnosisDetailsDTO`
     * class.
     * </p>
     */
    public DiagnosisDetailsDTO() {
    }

}
