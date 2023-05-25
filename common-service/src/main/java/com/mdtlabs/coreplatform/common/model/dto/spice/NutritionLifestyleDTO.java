package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO class for nutrition lifestyle entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class NutritionLifestyleDTO {
    private long id;

    private String name;

    private Integer displayOrder;

    private String cultureValue;
}
