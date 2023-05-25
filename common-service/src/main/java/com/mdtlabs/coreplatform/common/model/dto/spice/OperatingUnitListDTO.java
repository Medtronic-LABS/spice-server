package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is an DTO class for Operating Unit entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class OperatingUnitListDTO {
    private long id;
    private String name;
    private Long siteCount;
    private Long tenantId;

    /**
     * <p>
     * This  is a constructor method for the `OperatingUnitListDTO` class
     * </p>
     *
     * @param id       id param
     * @param name     name param
     * @param tenantId tenantId param
     */
    public OperatingUnitListDTO(long id, String name, Long tenantId) {
        super();
        this.id = id;
        this.name = name;
        this.tenantId = tenantId;
    }

    public OperatingUnitListDTO() {
    }

}
