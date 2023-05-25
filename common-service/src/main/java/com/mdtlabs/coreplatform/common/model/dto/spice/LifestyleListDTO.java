package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class is an DTO class for lifestyle entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class LifestyleListDTO {
    private Long id;

    private String name;

    private int displayOrder;

    private List<Map<String, Object>> answers;

    private String type;

    private String cultureValue;
}
