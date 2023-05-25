package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is a Data transfer object for Account Customization Entity.
 * </p>
 *
 * @author Niraimathi S created on 17 Feb 2023
 */
@Data
public class AccountCustomizationDTO {

    private Long id;

    private Long tenantId;

    private String type;

    private String category;

    private String formInput;

    private Long countryId;

    private Long clinicalWorkflowId;

    private Long accountId;

    private String workflow;

}
