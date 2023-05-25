package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and RiskAlgorithm entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Repository
public interface RiskAlgorithmRepository extends JpaRepository<RiskAlgorithm, Long> {

    /**
     * <p>
     * Gets list of RiskAlgorithm by country id.
     * </p>
     *
     * @return {@link List<RiskAlgorithm>} List of RiskAlgorithm entities
     */
    List<RiskAlgorithm> findByIsDeletedFalseAndIsActiveTrue();
}
