package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The OperatingUnitDTO class represents a data transfer object for an operating unit with its name,
 * ID, tenant ID, parent organization, and country ID.
 * </p>
 */
@Data
public class OperatingUnitDTO {

    private Long id;

    private String name;

    private Long tenantId;

    private ParentOrganizationDTO account;

    private Long countryId;

}