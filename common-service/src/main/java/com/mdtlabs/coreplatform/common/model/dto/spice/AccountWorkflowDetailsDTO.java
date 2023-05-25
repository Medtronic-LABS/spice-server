package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class is an DTO for AccountWorkflow details.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class AccountWorkflowDetailsDTO {
    private Long id;

    private String name;

    private String workflow;

    private String moduleType;

    private boolean isDefault;
}
