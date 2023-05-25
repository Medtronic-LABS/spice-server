package com.mdtlabs.coreplatform.common.controller;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;
import com.mdtlabs.coreplatform.common.service.GenericTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * Generic Controller used to perform any action like read and write for tenant based entities.
 * </p>
 *
 * @author Niraimathi S created on Dec 21, 2022
 */
@ResponseBody
public class GenericTenantController<T extends TenantBaseEntity> {

    /**
     * <p>
     * Java code that declares a private instance variable named genericTenantService of type GenericTenantService<T>.
     * It is then autowired using Spring's dependency injection to perform CRUD operations on tenant-based entities,
     * with <T> being a generic type parameter.
     * </p>
     */
    @Autowired
    private GenericTenantService<T> genericTenantService;

    /**
     * <p>
     * This method is used to add new entity
     * </p>
     *
     * @param entity {@link T} entity to be saved
     * @return {@link ResponseEntity<Object>}  entity response
     */
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody T entity) {
        try {
            return new ResponseEntity<>(genericTenantService.save(entity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <p>
     * This method is used to select entity values
     * </p>
     *
     * @return {@link ResponseEntity<List<T>>} - entity object
     */
    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        try {
            return new ResponseEntity<>(genericTenantService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <p>
     * This method is used to get entity with respect to id
     * </p>
     *
     * @param id - id of the entity
     * @return {@link ResponseEntity<T>} - response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable(FieldConstants.ID) Long id) {
        try {
            T entity = genericTenantService.findById(id);
            if (entity != null) {
                return new ResponseEntity<>(entity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
