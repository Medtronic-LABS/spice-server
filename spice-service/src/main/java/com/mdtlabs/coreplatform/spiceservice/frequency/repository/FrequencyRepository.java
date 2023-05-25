package com.mdtlabs.coreplatform.spiceservice.frequency.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This repository maintains connection between database and frequency entity.
 * </p>
 *
 * @author Niraimathi S
 * @since Jun 20, 2022
 */
@Repository
public interface FrequencyRepository extends JpaRepository<Frequency, Long> {

    /**
     * <p>
     * Find list of Frequency by its deleted and active status.
     * </p>
     *
     * @return {@link List<Frequency>} List of Frequency
     */
    List<Frequency> findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc();

}
