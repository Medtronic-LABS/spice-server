package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Country is a Java class representing a Country entity with properties such as name,
 * countryCode, and unitMeasurement.
 * </p>
 *
 * @author VigneshKumar created on Jun 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_COUNTRY)
public class Country extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = ErrorConstants.COUNTRY_NAME_NOT_NULL)
    @Column(name = FieldConstants.NAME)
    private String name;

    @NotEmpty(message = ErrorConstants.COUNTRY_CODE_NOT_NULL)
    @Column(name = FieldConstants.COUNTRY_CODE)
    private String countryCode;

    @NotEmpty(message = ErrorConstants.UNIT_MEASUREMENT_NOT_NULL)
    @Column(name = FieldConstants.UNIT_MEASUREMENT)
    private String unitMeasurement;

    public Country(Long countryId) {
        super(countryId);
    }

    public Country() {
    }
}
