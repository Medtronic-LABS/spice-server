package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO class for Diagnosis entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class DiagnosisListDTO {
    private Long id;

    private String name;

    private String type;

    private String gender;

    private int displayOrder;

    private String cultureValue;
}
