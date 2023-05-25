package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * This is a Java class representing a site organization with various properties and validation
 * annotations.
 * </p>
 */
@Data
public class SiteOrganizationDTO {
    @Size(min = 1, message = ErrorConstants.USER_MIN_SIZE)
    @Valid
    private List<SiteUserDTO> users;

    @NotBlank(message = ErrorConstants.SITE_NAME_NOT_NULL)
    private String name;

    private String email;

    @NotBlank(message = ErrorConstants.ADDRESS_TYPE_NOT_NULL)
    private String addressType;

    @NotBlank(message = ErrorConstants.ADDRESS_USE_NOT_NULL)
    private String addressUse;

    @NotBlank(message = ErrorConstants.ADDRESS_NOT_NULL)
    private String address1;

    private String address2;

    private String latitude;

    private String longitude;

    private String city;

    @NotBlank(message = ErrorConstants.PHONE_NUMBER_NOT_NULL)
    private String phoneNumber;

    private float workingHours;

    @NotBlank(message = ErrorConstants.POSTAL_CODE_NOT_NULL)
    private String postalCode;

    private String siteLevel;

    @NotBlank(message = ErrorConstants.SITE_TYPE_NOT_NULL)
    private String siteType;

    @NotNull(message = ErrorConstants.COUNTRY_NOT_NULL)
    private Long countryId;

    @NotNull(message = ErrorConstants.COUNTY_NOT_NULL)
    private Long countyId;

    @NotNull(message = ErrorConstants.SUB_COUNTY_NOT_NULL)
    private Long subCountyId;

    @NotNull(message = ErrorConstants.ACCOUNT_ID_NOT_NULL)
    private Long accountId;

    @NotNull(message = ErrorConstants.OPERATING_UNIT_NOT_NULL)
    private Operatingunit operatingUnit;

    @NotNull(message = ErrorConstants.CULTURE_NOT_NULL)
    private Long cultureId;

    private Long parentOrganizationId;

    private Long tenantId;
}
