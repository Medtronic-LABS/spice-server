package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * This class is an common DTO used for customization requests.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 08, 2023
 */
@Data
public class CustomizationRequestDTO {

    @NotNull
    private Long countryId;

    private String type;

    private String category;

    private Long clinicalWorkflowId;

    private Long accountId;

    private Long tenantId;

    private Long cultureId;
}
