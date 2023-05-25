package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.MedicalComplianceRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.MedicalComplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for MedicalCompliance module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class MedicalComplianceServiceImpl extends GenericServiceImpl<MedicalCompliance> implements MedicalComplianceService {

    @Autowired
    private MedicalComplianceRepository medicalComplianceRepository;

    /**
     * {@inheritDoc}
     */
    public List<MedicalCompliance> getMedicalComplianceList() {
        if (Constants.MEDICAL_COMPLIANCES.isEmpty()) {
            Constants.MEDICAL_COMPLIANCES.addAll(medicalComplianceRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.MEDICAL_COMPLIANCES;
    }


}
