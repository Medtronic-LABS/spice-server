package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * LabTestResultRange is a Java class representing a lab test result range entity with various
 * properties such as lab test ID, minimum and maximum values, unit, display order,
 * and display name.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 08, 2023
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_LAB_TEST_RESULT_RANGE)
public class LabTestResultRange extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = FieldConstants.LAB_TEST_ID)
    private Long labTestId;

    @NotNull
    @Column(name = FieldConstants.LAB_TEST_RESULT_ID)
    private Long labTestResultId;

    @NotNull(message = "Labtest result range minimum value")
    @Column(name = FieldConstants.MINIMUM_VALUE)
    private Double minimumValue;

    @NotNull(message = "Labtest result range maximum value")
    @Column(name = FieldConstants.MAXIMUM_VALUE)
    private Double maximumValue;

    @NotBlank(message = "Labtest result range unit")
    @Column(name = FieldConstants.UNIT)
    private String unit;

    @NotNull(message = "Labtest result range unit id")
    @Column(name = FieldConstants.UNIT_ID)
    private Long unitId;

    @NotNull(message = "Labtest result range display order")
    @Column(name = FieldConstants.DISPLAY_ORDER)
    private Integer displayOrder;

    @NotBlank(message = "Labtest result range display name")
    @Column(name = FieldConstants.DISPLAY_NAME)
    private String displayName;
}
