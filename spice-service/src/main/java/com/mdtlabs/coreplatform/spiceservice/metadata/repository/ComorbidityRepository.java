package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface is responsible for performing database operations between
 * server and Comorbidity entity.
 *
 * @author Niraimathi S
 */
@Repository
public interface ComorbidityRepository extends GenericRepository<Comorbidity> {

    /**
     * <p>
     * Gets list of comorbidity by isDeleted and isActive fields.
     * </p>
     *
     * @return List of Comorbidity entity.
     */
    List<Comorbidity> findByIsDeletedFalseAndIsActiveTrue();
}
