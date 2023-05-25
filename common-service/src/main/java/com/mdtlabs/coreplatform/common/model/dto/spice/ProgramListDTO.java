package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * The ProgramListDTO class contains fields for program information such as id, name, tenantId,
 * isActive, and createdAt.
 * </p>
 */
@Data
public class ProgramListDTO {
    private Long id;

    private String name;

    private Long tenantId;

    private boolean isActive;

    private Date createdAt;
}
