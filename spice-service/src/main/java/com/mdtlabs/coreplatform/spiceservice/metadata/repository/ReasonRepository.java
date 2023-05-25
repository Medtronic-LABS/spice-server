package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and reason entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Repository
public interface ReasonRepository extends GenericRepository<Reason> {

    /**
     * <p>
     * Gets list of Reason entities based on isDefault and isDeleted.
     * </p>
     *
     * @return {@link List<Reason>} List of Reason entities
     */
    List<Reason> findByIsDeletedFalseAndIsActiveTrue();

}
