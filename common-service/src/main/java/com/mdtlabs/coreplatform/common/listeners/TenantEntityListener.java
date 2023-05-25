package com.mdtlabs.coreplatform.common.listeners;

import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.util.Objects;

/**
 * <p>
 * This class is for tenant entity listener
 * </p>
 * 
 * @author VigneshKumar created on Jun 30, 2022
 */
public class TenantEntityListener {

/**
 * <p>
 * This function sets the tenant ID for a given object before it is persisted or updated in the
 * database.
 * </p>
 * 
 * @param object The "object" parameter is an instance of an entity class that is being persisted or
 * updated in the database is given
 */
    @PrePersist
    @PreUpdate
    public void prePersistAndUpdate(Object object) {
        if (object instanceof TenantBaseEntity tenantBaseEntity && ((TenantBaseEntity) object).getTenantId() == null) {
            tenantBaseEntity.setTenantId(UserSelectedTenantContextHolder.get());
        }
    }

    /**
     * <p>
     * This is a pre-remove hook in Java that checks if the entity being removed belongs to the current
     * tenant, and throws an exception if it doesn't.
     * </p>
     * 
     * @param object The object being removed from the database.
     */
    @PreRemove
    public void preRemove(Object object) {
        if (object instanceof TenantBaseEntity tenantBaseEntity
                && !Objects.equals(tenantBaseEntity.getTenantId(), UserSelectedTenantContextHolder.get())) {
            throw new EntityNotFoundException();
        }
    }
}