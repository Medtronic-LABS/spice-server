package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * This is a Java class representing an account workflow with various properties such as name, maximum
 * number of users, country ID, clinical workflow, customized workflow, reason, status, and tenant ID.
 * </p>
 */
@Data
public class AccountWorkflowDTO {

    private Long id;

    @NotBlank(message = ErrorConstants.ACCOUNT_NAME_NOT_NULL)
    private String name;

    private int maxNoOfUsers;

    private boolean isUsersRestricted;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    private Long countryId;

    @NotNull(message = ErrorConstants.CLINICAL_WORKFLOW_NOT_NULL)
    private List<Long> clinicalWorkflow;

    private List<Long> customizedWorkflow;

    private String reason;

    private String status;

    private Long tenantId;
}
