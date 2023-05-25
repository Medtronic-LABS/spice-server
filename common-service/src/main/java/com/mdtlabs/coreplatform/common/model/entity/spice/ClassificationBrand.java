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
 * ClassificationBrand is a Java class representing a classification brand entity with properties
 * such as country ID, brand, and classification ID.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Entity
@Table(name = TableConstants.TABLE_CLASSIFICATION_BRAND)
@Data
public class ClassificationBrand extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @JoinColumn(name = FieldConstants.BRAND_ID)
    @ManyToOne
    private Brand brand;

    @Column(name = FieldConstants.CLASSIFICATION_ID)
    private Long classificationId;
}
