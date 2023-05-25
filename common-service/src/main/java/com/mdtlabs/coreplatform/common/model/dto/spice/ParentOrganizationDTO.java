package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is a Java class representing a parent organization with an ID, name, and tenant ID.
 * </p>
 */
@Data
public class ParentOrganizationDTO {
    private Long id;
    private String name;
    private Long tenantId;
}
