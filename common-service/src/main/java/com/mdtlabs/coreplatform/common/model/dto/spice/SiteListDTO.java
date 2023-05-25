package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO for Site entity.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Data
public class SiteListDTO {
    private Long id;

    private String name;

    private Long tenantId;

    private String operatingUnitName;

    private String cultureName;

    private String siteLevel;

    private String siteType;
}
