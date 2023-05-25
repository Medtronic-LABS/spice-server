package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is an DTO class for Country entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class CountryListDTO {
    private long id;
    private String name;
    private Long accountsCount;
    private Long ouCount;
    private Long siteCount;
    private Long tenantId;
}
