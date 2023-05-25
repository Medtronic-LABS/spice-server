package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PhysicalExaminationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.PhysicalExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This service class contain all the business logic for physical examination
 * module and perform all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class PhysicalExaminationServiceImpl implements PhysicalExaminationService {

    @Autowired
    private PhysicalExaminationRepository physicalExaminationRepository;

    /**
     * {@inheritDoc}
     */
    public List<PhysicalExamination> getPhysicalExamination() {
        if (Constants.PHYSICAL_EXAMINATIONS.isEmpty()) {
            Constants.PHYSICAL_EXAMINATIONS.addAll(physicalExaminationRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.PHYSICAL_EXAMINATIONS;
    }

    /**
     * {@inheritDoc}
     */
    public Set<PhysicalExamination> getPhysicalExaminationByIds(Set<Long> ids) {
        return physicalExaminationRepository.findByIdInAndIsDeletedFalseAndIsActiveTrue(ids);
    }

}
