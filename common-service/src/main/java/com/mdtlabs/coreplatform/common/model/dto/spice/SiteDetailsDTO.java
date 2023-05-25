package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Map;

/**
 * <p>
 * The SiteDetailsDTO class is a data transfer object that contains various details about a site,
 * including its name, address, location, and parent organizations.
 * </p>
 */
@Data
public class SiteDetailsDTO {

    private Long id;

    private String name;

    private String email;

    private String addressType;

    private String addressUse;

    private String address1;

    private String address2;

    private String latitude;

    private String longitude;

    private Map<String, Object> city;

    private String phoneNumber;

    private String postalCode;

    private Map<String, Object> siteLevel;

    private String siteType;

    private ParentOrganizationDTO country;

    private ParentOrganizationDTO county;

    private ParentOrganizationDTO subCounty;

    private ParentOrganizationDTO account;

    private ParentOrganizationDTO operatingUnit;

    private ParentOrganizationDTO culture;

    private Long tenantId;
}
