package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The MedicalComplianceDto class contains three fields: isChildExists (a Boolean), name (a String),
 * and complianceId (a Long).
 * </p>
 */
@Data
public class MedicalComplianceDto {

    private Boolean isChildExists;

    private String name;

    private Long complianceId;

}
