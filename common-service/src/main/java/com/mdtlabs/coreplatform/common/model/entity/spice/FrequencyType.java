package com.mdtlabs.coreplatform.common.model.entity.spice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

import lombok.Data;

/**
 * <p>
 * FrequencyType is a Java class representing a frequencytype entity with various fields and annotations.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_FREQUENCY_TYPE)
public class FrequencyType extends BaseEntity {

    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.CULTURE_VALUE)
    private String cultureValue;

    @Column(name = FieldConstants.CULTURE_ID)
    private Long cultureId;
    
}
