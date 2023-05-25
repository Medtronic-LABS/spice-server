package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * AccountWorkflow is a Java class representing an account workflow with various fields and constraints.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_ACCOUNT_WORKFLOW, uniqueConstraints = {
        @UniqueConstraint(name = TableConstants.NAME_AND_COUNTRY_ID, columnNames = {FieldConstants.NAME,
                FieldConstants.COUNTRY_ID})})
@TypeDef(name = Constants.LIST_ARRAY, typeClass = ListArrayType.class)
@DynamicUpdate
public class AccountWorkflow extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = ErrorConstants.ACCOUNT_WORKFLOW_NAME_NOT_EMPTY)
    @Column(name = FieldConstants.NAME)
    private String name;

    @NotNull(message = ErrorConstants.ACCOUNT_WORKFLOW_VIEW_SCREEN_NOT_NULL)
    @Size(min = 1, message = ErrorConstants.ACCOUNT_WORKFLOW_VIEW_SCREEN_NOT_NULL)
    @Type(type = Constants.LIST_ARRAY)
    @Column(name = FieldConstants.VIEW_SCREENS, columnDefinition = Constants.COLUMN_DEFINITION_TEXT)
    private List<String> viewScreens;

    @Column(name = FieldConstants.WORKFLOW)
    private String workflow;

    @Column(name = FieldConstants.MODULE_TYPE)
    private String moduleType;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @Column(name = FieldConstants.IS_DEFAULT)
    private boolean isDefault;
}
