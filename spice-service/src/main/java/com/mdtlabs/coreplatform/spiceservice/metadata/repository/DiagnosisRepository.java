package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and Diagnosis entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Repository
public interface DiagnosisRepository extends GenericRepository<Diagnosis> {

    /**
     * <p>
     * This method is used to get list of Diagnosis entities.
     * </p>
     *
     * @return {@link List<Diagnosis>} List of Diagnosis
     */
    List<Diagnosis> findByIsDeletedFalseAndIsActiveTrue();

}
