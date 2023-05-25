package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The ComorbidityDTO class contains a comorbidity ID and a string for other comorbidities.
 * </p>
 */
@Data
public class ComorbidityDTO {

    private Long comorbidityId;

    private String otherComorbity;
}
 