package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and MedicalCompliance entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Repository
public interface MedicalComplianceRepository extends GenericRepository<MedicalCompliance> {

    /**
     * <p>
     * Gets list of MedicalCompliance entities.
     * </p>
     *
     * @return {@link List<MedicalCompliance>} MedicalCompliance entities List
     */
    List<MedicalCompliance> findByIsDeletedFalseAndIsActiveTrue();

}
