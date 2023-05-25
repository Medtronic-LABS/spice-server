package com.mdtlabs.coreplatform.common.service.impl;

import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import com.mdtlabs.coreplatform.common.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * This service class contain all the business logic and perform all the
 * operation here.
 * </p>
 *
 * @author Prabu created on Oct 20, 2022
 */
@Service
public class GenericServiceImpl<T extends BaseEntity> implements GenericService<T> {

    @Autowired
    private GenericRepository<T> genericRepository;

    /**
     * <p>
     * This function returns a list of all objects of type T using a generic repository and logs any
     * exceptions that occur.
     * </p>
     * 
     * @return A list of objects of type T is being returned. The specific type of T is not specified
     * in this code snippet as it is a generic method.
     */
    @Override
    public List<T> findAll() {
        try {
            return genericRepository.findAll();
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
     * @param id The parameter "id" is a Long data type representing the unique identifier of an entity
     * that needs to be retrieved from the database
     * @return {@link T} The method is returning an object of type T, which is the type parameter of the class that
     * contains this method
     */
    @Override
    public T findById(Long id) {
        try {
            Optional<T> entity = genericRepository.findById(id);
            return entity.orElse(null);
        } catch (Exception exception) {
            Logger.logError(exception);
            throw exception;
        }
    }

    /**
     * <p>
     * This function saves an entity and logs any errors that occur.
     * </p>
     * 
     * @param entity The parameter "entity" is an object of type T, which is a generic type parameter is given
     * @return {@link T}The method is returning an object of type T, which is the same type as the entity being
     * passed as a parameter to the method is given
     */
    @Override
    public T save(T entity) {
        try {
            return genericRepository.save(entity);
        } catch (Exception exception) {
            Logger.logError(exception);
            throw exception;
        }
    }

    /**
     * <p>
     * This function returns a list of objects sorted by a specified criteria, and logs any exceptions
     * that occur.
     * </p>
     * 
     * @param sort The "sort" parameter is an object of the Sort class which is used to specify the
     * sorting criteria for the query results
     * @return {@link List<T>} A List of objects of type T is being returned
     */
    public List<T> findAll(Sort sort) {
        try {
            return genericRepository.findAll(sort);
        } catch (Exception exception) {
            Logger.logError(exception);
            throw exception;
        }
    }

}
