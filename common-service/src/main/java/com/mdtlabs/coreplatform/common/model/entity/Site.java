package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Site is a Java class representing a site entity with various attributes such as name, address, latitude, longitude,
 * phone number, and cultural information.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_SITE)
public class Site extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = ErrorConstants.SITE_NAME_NOT_NULL)
    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.EMAIL)
    private String email;

    @NotBlank(message = ErrorConstants.ADDRESS_TYPE_NOT_NULL)
    @Column(name = FieldConstants.ADDRESS_TYPE)
    private String addressType;

    @NotBlank(message = ErrorConstants.ADDRESS_USE_NOT_NULL)
    @Column(name = FieldConstants.ADDRESS_USE)
    private String addressUse;

    @NotBlank(message = ErrorConstants.ADDRESS_NOT_NULL)
    @Column(name = FieldConstants.ADDRESS_1)
    private String address1;

    @Column(name = FieldConstants.ADDRESS_2)
    private String address2;

    @Column(name = FieldConstants.LATITUDE)
    private String latitude;

    @Column(name = FieldConstants.LONGITUDE)
    private String longitude;

    @Column(name = FieldConstants.CITY)
    private String city;

    @NotBlank(message = ErrorConstants.PHONE_NUMBER_NOT_NULL)
    @Column(name = FieldConstants.PHONE_NUMBER)
    private String phoneNumber;

    @Column(name = FieldConstants.WORKING_HOURS)
    private Float workingHours;

    @NotNull(message = ErrorConstants.POSTAL_CODE_NOT_NULL)
    @Column(name = FieldConstants.POSTAL_CODE)
    private String postalCode;

    @NotBlank(message = ErrorConstants.SITE_LEVEL_NOT_NULL)
    @Column(name = FieldConstants.SITE_LEVEL)
    private String siteLevel;

    @NotBlank(message = ErrorConstants.SITE_TYPE_NOT_NULL)
    @Column(name = FieldConstants.SITE_TYPE)
    private String siteType;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @NotNull(message = ErrorConstants.COUNTY_NOT_NULL)
    @Column(name = FieldConstants.COUNTY_ID)
    private Long countyId;

    @NotNull(message = ErrorConstants.SUB_COUNTY_NOT_NULL)
    @Column(name = FieldConstants.SUB_COUNTY_ID)
    private Long subCountyId;

    @NotNull(message = ErrorConstants.ACCOUNT_ID_NOT_NULL)
    @Column(name = FieldConstants.ACCOUNT_ID)
    private Long accountId;

    @NotNull(message = ErrorConstants.OPERATING_UNIT_ID_NOT_NULL)
    @ManyToOne
    @JoinColumn(name = FieldConstants.OPERATING_UNIT_ID)
    private Operatingunit operatingUnit;

    @NotNull(message = ErrorConstants.CULTURE_NOT_NULL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FieldConstants.CULTURE_ID)
    private Culture culture;

    public Site() {
    }

    public Site(Long id) {
        super(id);
    }
}
