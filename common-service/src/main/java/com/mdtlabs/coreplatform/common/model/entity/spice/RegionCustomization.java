package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * RegionCustomization is a Java class representing a region customization entity with various
 * fields such as type, category, form input, country ID, and culture ID.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_REGION_CUSTOMIZATION)
public class RegionCustomization extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = -8642271760727722638L;

    @NotBlank(message = ErrorConstants.TYPE_NOT_NULL)
    @Column(name = FieldConstants.TYPE)
    private String type;

    @NotBlank(message = ErrorConstants.CATEGORY_NOT_NULL)
    @Column(name = FieldConstants.CATEGORY)
    private String category;

    @NotBlank(message = ErrorConstants.FORM_INPUT_NOT_NULL)
    @Column(name = FieldConstants.FORM_INPUT)
    private String formInput;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @Column(name = FieldConstants.IS_DEFAULT)
    private boolean isDefault;

    @Column(name = FieldConstants.CULTURE_ID)
    private Long cultureId;
}
