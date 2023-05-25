package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and CurrentMedication entity.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Repository
public interface CurrentMedicationRepository extends GenericRepository<CurrentMedication> {

    /**
     * <p>
     * Gets CurrentMedication entity list based on isActive And isDeleted fields.
     * </p>
     *
     * @return {@link List<CurrentMedication>} List of CurrentMedication entities
     */
    List<CurrentMedication> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * Gets CurrentMedication entity list based on isActive, isDeleted, and id's.
     * </p>
     *
     * @param ids {@link Set<Long>} ids
     * @return {@link List<CurrentMedication>} List of CurrentMedication entities
     */
    List<CurrentMedication> findByIdInAndIsDeletedFalseAndIsActiveTrue(Set<Long> ids);

}
