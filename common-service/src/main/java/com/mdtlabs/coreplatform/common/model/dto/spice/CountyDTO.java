package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an Data transfer object for County Entity.
 * </p>
 *
 * @author Niraimathi S created on 17 Feb 2023
 */
@Data
public class CountyDTO {

    private Long id;

    private String name;

    private int displayOrder;

    private Long countryId;
}
