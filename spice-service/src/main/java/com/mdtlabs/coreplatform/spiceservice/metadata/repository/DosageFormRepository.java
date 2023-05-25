package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and DosageForm entity.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Repository
public interface DosageFormRepository extends GenericRepository<DosageForm> {

    /**
     * <p>
     * Gets list of Dosage form entities except "Other" dosage form.
     * </p>
     *
     * @param name {@link String} dosage form name
     * @return {@link List<DosageForm>} List of DosageForm entities
     */
    List<DosageForm> findByNameNotLike(String name);


    /**
     * <p>
     * Gets list of Dosage form entities except "Other" dosage form.
     * </p>
     *
     * @return {@link List<DosageForm>} List of DosageForm entities
     */
    List<DosageForm> findByIsDeletedFalseAndIsActiveTrue();

}
