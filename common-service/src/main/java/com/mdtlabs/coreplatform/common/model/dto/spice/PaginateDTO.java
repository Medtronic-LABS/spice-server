package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Map;

/**
 * <p>
 * The PaginateDTO class is a data transfer object that contains information for pagination, including
 * search term, limit, skip, and sort.
 * </p>
 */
@Data
public class PaginateDTO {

    private String searchTerm;

    private int limit;

    private int skip;

    private Map<String, String> sort;
}
