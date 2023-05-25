package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * The OperatingUnitDetailsDTO class contains information about an operating unit, including its ID,
 * name, tenant ID, a list of users, and its parent organization.
 * </p>
 */
@Data
public class OperatingUnitDetailsDTO {
    private Long id;
    private String name;
    private Long tenantId;
    private List<UserOrganizationDTO> users;
    private ParentOrganizationDTO account;

}