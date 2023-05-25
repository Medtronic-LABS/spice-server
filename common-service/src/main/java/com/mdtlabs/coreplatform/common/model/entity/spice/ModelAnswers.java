package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

/**
 * <p>
 * ModelAnswers is a Java class representing a table called "ModelAnswers" with columns for answer,
 * display order, default status, value, culture value, and culture ID.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.MODEL_ANSWERS)
public class ModelAnswers extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.ANSWER)
    private String answer;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;

    @Column(name = FieldConstants.IS_DEFAULT)
    private boolean isDefault;

    @Column(name = FieldConstants.VALUE)
    private int value;

    @Column(name = FieldConstants.CULTURE_VALUE)
    private String cultureValue;

    @Column(name = FieldConstants.CULTURE_ID)
    private Long cultureId;
}