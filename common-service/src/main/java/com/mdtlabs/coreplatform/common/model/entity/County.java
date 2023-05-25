package com.mdtlabs.coreplatform.common.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * County is a Java class representing a County entity with properties such as name, display order, and country ID.
 * </p>
 *
 * @author VigneshKumar created on Jun 2022
 */
@Entity
@Data
@Table(name = TableConstants.TABLE_COUNTY)
public class County extends BaseEntity {

    @NotEmpty(message = ErrorConstants.COUNTY_NAME_NOT_NULL)
    @Column(name = FieldConstants.NAME)
    private String name;

    @Min(value = Constants.ONE, message = ErrorConstants.DISPLAY_ORDER_MIN_VALUE)
    @NotNull(message = ErrorConstants.DISPLAY_ORDER_NOT_NULL)
    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;

    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;
}
