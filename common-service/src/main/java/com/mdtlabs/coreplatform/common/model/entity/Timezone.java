package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Timezone is a Java class representing a timezone entity with abbreviation, description, and offset properties.
 * </p>
 *
 * @author ArunKarthik created on Aug 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_TIMEZONE)
public class Timezone extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.ABBREVIATION)
    private String abbreviation;

    @Column(name = FieldConstants.DESCRIPTION)
    private String description;

    @Column(name = FieldConstants.OFFSET)
    private String offset;

    public Timezone(Long id) {
        super(id);
    }

    public Timezone() {
        super();
    }
}
