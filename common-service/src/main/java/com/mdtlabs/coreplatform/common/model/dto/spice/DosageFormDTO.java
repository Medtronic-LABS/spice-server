package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO class for dosageForm entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class DosageFormDTO {
    private Long id;

    private String name;

    private int displayOrder;

    private String cultureValue;

}
