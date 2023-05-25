package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface is responsible for performing database operations between
 * server and Complication entity.
 *
 * @author Niraimathi S created on 30 Jun, 2022
 */
@Repository
public interface ComplicationRepository extends GenericRepository<Complication> {

    /**
     * <p>
     * Gets Complication entity list by isDeleted and isActive fields.
     * </p>
     *
     * @return {@param List<Complication>} List of Complication entities.
     */
    List<Complication> findByIsDeletedFalseAndIsActiveTrue();

}
