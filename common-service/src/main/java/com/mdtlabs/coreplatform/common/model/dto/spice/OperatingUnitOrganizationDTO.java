package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * This is a Java class representing an Operating Unit Organization with a name, account, country ID,
 * tenant ID, parent organization ID, and a list of user organization DTOs.
 * </p>
 */
@Data
public class OperatingUnitOrganizationDTO {
    @NotBlank(message = ErrorConstants.ACCOUNT_NAME_NOT_NULL)
    private String name;

    private Account account;

    private Long countryId;

    private Long tenantId;

    private Long parentOrganizationId;

    @Size(min = 1, message = ErrorConstants.USER_MIN_SIZE)
    @Valid
    private List<UserOrganizationDTO> users;
}
