package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * This is an DTO class for Country entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class CountryDTO {
    private Long id;

    @NotEmpty(message = ErrorConstants.COUNTRY_NAME_NOT_NULL)
    private String name;

    @NotEmpty(message = ErrorConstants.COUNTRY_CODE_NOT_NULL)
    private String countryCode;

    @NotEmpty(message = ErrorConstants.UNIT_MEASUREMENT_NOT_NULL)
    private String unitMeasurement;

    private Long tenantId;

}
