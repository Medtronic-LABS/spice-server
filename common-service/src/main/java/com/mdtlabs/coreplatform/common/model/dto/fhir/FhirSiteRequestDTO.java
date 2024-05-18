package com.mdtlabs.coreplatform.common.model.dto.fhir;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FhirSiteRequestDTO {
    private @NotNull(
            message = "Type should not be null"
    ) String type;

    private Long id;

    private String name;

    private String addressType;

    private String addressUse;

    private String address1;

    private String address2;

    private String latitude;

    private String longitude;

    private String city;

    private String phoneNumber;

    private Float workingHours;

    private String postalCode;

    private String siteType;

    private Long countryId;

    private Long countyId;

    private Long subCountyId;

    private String mflCode;

    private boolean isActive;

    private String countryName;

    private String subCountyName;

    private String countyName;

}
