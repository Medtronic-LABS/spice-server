package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO class for Medical compliance entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class MedicalComplianceListDTO {
    private Long id;

    private String name;

    private Long parentComplianceId;

    private boolean isChildExists;

    private int displayOrder;

    private String cultureValue;
}
