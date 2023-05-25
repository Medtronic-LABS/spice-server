package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * This is a Java class representing an account organization with various properties such as name,
 * maximum number of users, country ID, clinical workflow, and a list of users.
 * </p>
 */
@Data
public class AccountOrganizationDTO {
    private Long id;

    @NotBlank(message = ErrorConstants.ACCOUNT_NAME_NOT_NULL)
    private String name;

    private int maxNoOfUsers;

    private Long tenantId;

    private boolean isUsersRestricted;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    private Long countryId;

    @NotNull(message = ErrorConstants.CLINICAL_WORKFLOW_NOT_NULL)
    private List<Long> clinicalWorkflow;

    private List<Long> customizedWorkflow;

    @Size(min = 1, message = ErrorConstants.USER_MIN_SIZE)
    @Valid
    private List<UserOrganizationDTO> users;

    private String countryCode;

    @NotNull(message = ErrorConstants.PARENT_ORG_ID_NOT_NULL)
    private Long parentOrganizationId;
}
