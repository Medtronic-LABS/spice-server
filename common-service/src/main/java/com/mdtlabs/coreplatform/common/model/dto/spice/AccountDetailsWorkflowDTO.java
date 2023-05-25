package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * This is a Java class representing account details workflow data transfer object with various fields
 * including view screens, country ID, and default status.
 * </p>
 */
@Data
public class AccountDetailsWorkflowDTO {

    private Long id;

    private Long tenantId;

    private String name;

    @NotNull(message = ErrorConstants.ACCOUNT_WORKFLOW_VIEW_SCREEN_NOT_NULL)
    private List<String> viewScreens;

    private String workflow;

    private String moduleType;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    private Long countryId;

    private boolean isDefault;
}
