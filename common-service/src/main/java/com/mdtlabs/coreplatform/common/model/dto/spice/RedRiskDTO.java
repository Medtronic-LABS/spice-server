package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is a Data Transfer object for Red risk calculation.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class RedRiskDTO {

    private int comorbiditiesCount;

    private String diabetesDiagControlledType;

    private String diabetesPatientType;

    public RedRiskDTO(int comorbiditiesCount, String diabetesDiagControlledType, String diabetesPatientType) {
        this.comorbiditiesCount = comorbiditiesCount;
        this.diabetesDiagControlledType = diabetesDiagControlledType;
        this.diabetesPatientType = diabetesPatientType;
    }

    public RedRiskDTO() {
    }


}