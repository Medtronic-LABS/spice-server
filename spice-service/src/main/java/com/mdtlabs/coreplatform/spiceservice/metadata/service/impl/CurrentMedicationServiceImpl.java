package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.CurrentMedicationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CurrentMedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This service class contain all the business logic for current medication module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class CurrentMedicationServiceImpl extends GenericServiceImpl<CurrentMedication>
        implements CurrentMedicationService {

    @Autowired
    private CurrentMedicationRepository currentMedicationRepository;

    /**
     * {@inheritDoc}
     */
    public List<CurrentMedication> findByIsDeletedFalseAndIsActiveTrue() {
        return currentMedicationRepository.findByIsDeletedFalseAndIsActiveTrue();
    }

    /**
     * {@inheritDoc}
     */
    public List<CurrentMedication> getCurrentMedicationByIds(Set<Long> ids) {
        return currentMedicationRepository.findByIdInAndIsDeletedFalseAndIsActiveTrue(ids);
    }
}
