package com.mdtlabs.coreplatform.common.service;

import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

import java.util.List;

/**
 * <p>
 * This an interface class in which you can implemented this class in any
 * tenant based class.
 * </p>
 *
 * @author Niraimathi S created on Dec 21, 2022
 */
public interface GenericTenantService<T extends TenantBaseEntity> {
    /**
     * <p>
     * This method is used to get all entity values
     * </p>
     *
     * @return List of entities
     */
    List<T> findAll();

    /**
     * <p>
     * This method is used to save the entity value
     * </p>
     *
     * @param entity  value to be saved
     * @return entity  saved entity
     */
    T save(T entity);

    /**
     * <p>
     * This method is used to fetch an entity
     * </p>
     *
     * @param id  id of the entity to be fetched
     * @return entity
     */
    T findById(Long id);


}
