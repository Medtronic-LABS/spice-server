package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * LabTestResult is a Java class representing a Lab Test Result entity with fields for name,
 * lab test ID, and display order.
 * </p>
 *
 * @author ArunKarthik created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_LAB_TEST_RESULT)
@Validated
public class LabTestResult extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = -6234779218896037042L;

    @NotBlank(message = "LabTest Result name should not be empty")
    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.LAB_TEST_ID)
    private Long labTestId;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;
}
