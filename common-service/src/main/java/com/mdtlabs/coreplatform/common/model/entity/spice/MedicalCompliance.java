package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

/**
 * <p>
 * MedicalCompliance is a Java class representing a medical compliance entity with various fields.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_MEDICAL_COMPLIANCE)
public class MedicalCompliance extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.STATUS)
    private boolean status;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;

    @Column(name = FieldConstants.IS_CHILD_EXISTS)
    private boolean isChildExists;

    @Column(name = FieldConstants.PARENT_COMPLIANCE_ID)
    private Long parentComplianceId;

    @Transient
    private String cultureValue;
}
