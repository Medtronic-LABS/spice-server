package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and Lifestyle entity.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Repository
public interface LifestyleRepository extends GenericRepository<Lifestyle> {

    /**
     * <p>
     * Gets list of Lifestyle entities based on isActive and isDeleted fields.
     * </p>
     *
     * @return {@link List<Lifestyle>} Lifestyle entities List
     */
    List<Lifestyle> findByIsDeletedFalseAndIsActiveTrue();

}
