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
 * Reason is a Java class representing a Reason entity with fields for name, type, display order,
 * and a transient culture value.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 07, 2023
 */
@Entity
@Table(name = TableConstants.TABLE_REASON)
@Data
public class Reason extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;

    @Transient
    private String cultureValue;
}
