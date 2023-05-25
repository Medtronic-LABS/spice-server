package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * This is an DTO class for Country entity.
 * </p>
 *
 * @author Niraimathi S created on Jun 2022
 */
@Data
public class CountryOrganizationDTO {

    private long id;

    @NotEmpty(message = ErrorConstants.COUNTRY_NAME_NOT_NULL)
    private String name;

    @NotEmpty(message = ErrorConstants.COUNTRY_CODE_NOT_NULL)
    private String countryCode;

    @NotEmpty(message = ErrorConstants.UNIT_MEASUREMENT_NOT_NULL)
    private String unitMeasurement;

    @Size(min = Constants.ONE, message = ErrorConstants.USER_MIN_SIZE)
    @Valid
    private List<UserOrganizationDTO> users;

    private Long tenantId;
}
