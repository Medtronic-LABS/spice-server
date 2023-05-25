package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The SearchRequestDTO class extends PaginateDTO and contains various fields for search criteria.
 * </p>
 */
@Data
public class SearchRequestDTO extends PaginateDTO {

    private Long countryId;

    private Long operatingUnitId;

    private Long accountId;

    private Long siteId;

    private Boolean isPaginated;

    private Long patientTrackId;

    private Long lastRefillVisitId;

    private Long tenantId;

    private String userType;

}
