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
 * AccountCustomization is a Java class representing an entity for account customization
 * with various fields such as type, category, form input, country ID,
 * clinical workflow ID, account ID, and workflow.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 08, 2023
 */
@Entity
@Data
@Table(name = TableConstants.TABLE_ACCOUNT_CUSTOMIZATION)
public class AccountCustomization extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @Column(name = FieldConstants.CLINICAL_WORKFLOW_ID)
    private Long clinicalWorkflowId;

    @Column(name = FieldConstants.ACCOUNT_ID)
    private Long accountId;

    @Column(name = FieldConstants.WORKFLOW)
    private String workflow;
}
