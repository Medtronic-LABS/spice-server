package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * CountryClassification is a Java entity class representing a country classification
 * with a classification object and a country ID.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Entity
@Table(name = TableConstants.TABLE_COUNTRY_CLASSIFICATION)
@Data
public class CountryClassification extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @JoinColumn(name = FieldConstants.CLASSIFICATION_ID)
    @ManyToOne
    private Classification classification;

    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;
}
