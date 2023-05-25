package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

/**
 * <p>
 * Symptom is a Java class representing a Symptom entity with various fields including name, description,
 * type, display order, categories, and culture value.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_SYMPTOM)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Symptom extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.DESCRIPTION)
    private String description;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private int displayOrder;

    @Type(type = "jsonb")
    @Column(name = FieldConstants.CATEGORIES, columnDefinition = "jsonb")
    private Map<String, Boolean> categories;

    @Transient
    private String cultureValue;
}
