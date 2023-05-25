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
 * Frequency is a Java class representing a frequency entity with various fields and annotations.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Entity
@Data
@Table(name = TableConstants.TABLE_FREQUENCY)
public class Frequency extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.DURATION)
    private Integer duration;

    @Column(name = FieldConstants.PERIOD)
    private String period;

    @Column(name = FieldConstants.RISK_LEVEL)
    private String riskLevel;

    @Column(name = FieldConstants.TITLE)
    private String title;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private Integer displayOrder;

    @Transient
    private String cultureValue;
}
