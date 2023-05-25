package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.listeners.TenantEntityListener;

/**
 * <p>
 * TenantBaseEntity is a Java class for a base entity with a tenant filter and listener.
 * </p>
 *
 * @author Vignesh created on July 30, 2022
 */
@Data
@MappedSuperclass
@FilterDef(name = Constants.TENANT_FILTER_NAME,
        parameters = @ParamDef(name = Constants.TENANT_PARAMETER_NAME, type = Constants.TENANT_PARAMETER_TYPE),
        //defaultCondition = Constants.TENANT_COLUMN_NAME + " in (:" + Constants.TENANT_PARAMETER_NAME + ")")
        defaultCondition = Constants.TENANT_COLUMN_NAME + " = :" + Constants.TENANT_PARAMETER_NAME)
@Filter(name = Constants.TENANT_FILTER_NAME)
@EntityListeners(TenantEntityListener.class)
public class TenantBaseEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.TENANT_ID)
    protected Long tenantId;

    public TenantBaseEntity(Long id) {
        super(id);
    }

    public TenantBaseEntity() {
    }
}
