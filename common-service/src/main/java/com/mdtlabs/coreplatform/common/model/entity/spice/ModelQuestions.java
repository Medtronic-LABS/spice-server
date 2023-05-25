package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

/**
 * <p>
 * ModelQuestions is a Java class representing model questions with various fields and a one-to-many
 * relationship with model answers.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Entity
@Data
@Table(name = TableConstants.MODEL_QUESTIONS)
public class ModelQuestions extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @Column(name = FieldConstants.QUESTIONS)
    private String questions;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;

    @Column(name = FieldConstants.IS_DEFAULT)
    private boolean isDefault;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.IS_MANDATORY)
    private boolean isMandatory;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = FieldConstants.QUESTION_ID)
    private List<ModelAnswers> modelAnswers;

    @Transient
    private String cultureValue;
}
