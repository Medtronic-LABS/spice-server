package com.mdtlabs.coreplatform.common.service.impl;

import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;
import com.mdtlabs.coreplatform.common.repository.GenericTenantRepository;
import com.mdtlabs.coreplatform.common.service.GenericTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * This service class contain all the business logic and perform all the
 * operation here.
 * </p>
 *
 * @author Niraimathi S created on Dec 20, 2022
 */
@Service
public class GenericTenantServiceImpl<T extends TenantBaseEntity> implements GenericTenantService<T> {
    @Autowired
    private GenericTenantRepository<T> genericTenantRepository;

    /**
     * <p>
     * This function returns a list of all objects of type T using a generic repository and logs any
     * exceptions.
     * </p>
     * 
     * @return A list of objects of type T is being returned. The specific type of T is not specified in
     * the code snippet.
     */
    @Override
    public List<T> findAll() {
        try {
            return genericTenantRepository.findAll();
        } catch (Exception exception) {
            Logger.logError(exception);
            throw exception;
        }
    }

    /**
     * <p>
     * This function finds an entity by its ID and returns it, or returns null if it doesn't exist.
     * </p>
     * 
     * @param id The parameter "id" is of type Long and represents the unique identifier of an entity
     * that needs to be retrieved from the database.
     * @return The method is returning an object of type T (which is a generic type) or null if the
     * entity with the given id is not found in the database.
     */
    @Override
    public T findById(Long id) {
        try {
            Optional<T> entity = genericTenantRepository.findById(id);
            return entity.orElse(null);
        } catch (Exception exception) {
            Logger.logError(exception);
            throw exception;
        }
    }

    /**
     * <p>
     * This function saves a generic entity and logs any errors that occur.
     * </p>
     * 
     * @param entity The parameter "entity" is an object of type T, which is a generic type parameter
     * is given
     * @return The method is returning an object of type T, which is the same type as the entity being
     * passed as a parameter to the method is given
     */
    @Override
    public T save(T entity) {
        try {
            return genericTenantRepository.save(entity);
        } catch (Exception exception) {
            Logger.logError(exception);
            throw exception;
        }
    }
}
