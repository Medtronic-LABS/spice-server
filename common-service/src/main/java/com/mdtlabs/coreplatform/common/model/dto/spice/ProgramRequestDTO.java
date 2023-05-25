package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * <p>
 * This is a Java class representing a program request with various properties such as name, country,
 * sites, and regionTenantId.
 * </p>
 */
@Data
public class ProgramRequestDTO {

    private Long id;

    @NotEmpty(message = ErrorConstants.PROGRAM_NAME_NOT_EMPTY)
    private String name;

    private Long tenantId;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    private Country country;

    private Set<Long> sites;

    private Set<Long> deletedSites;

    private Long regionTenantId;

    private boolean isActive;
}