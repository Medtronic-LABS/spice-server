package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This an interface class for current medication module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface CurrentMedicationService extends GenericService<CurrentMedication> {

    /**
     * <p>
     * Retrieves list of Current Medication for which isDelete is false
     * and is active is true.
     * </p>
     *
     * @return List of Current Medication    {@link List<CurrentMedication>} list of entity
     */
    List<CurrentMedication> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * Retrieves list of Current Medication by id's.
     * </p>
     *
     * @param ids ids
     * @return List of Current Medication    {@link List<CurrentMedication>} list of entity
     */
    List<CurrentMedication> getCurrentMedicationByIds(Set<Long> ids);

}
