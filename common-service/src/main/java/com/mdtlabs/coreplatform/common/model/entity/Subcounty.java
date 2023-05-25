package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Subcounty is a Java class representing a subcounty entity with properties such as name,
 * display order, country ID, and county ID.
 * </p>
 *
 * @author VigneshKumar created on Jun 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_SUB_COUNTY)
public class Subcounty extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = ErrorConstants.SUB_COUNTY_NAME_NOT_NULL)
    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;

    @NotNull(message = ErrorConstants.COUNTRY_NOT_NULL)
    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @NotNull(message = ErrorConstants.COUNTY_NOT_NULL)
    @Column(name = FieldConstants.COUNTY_ID)
    private Long countyId;
}
