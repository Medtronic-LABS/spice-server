package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.DiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for Diagnosis module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class DiagnosisServiceImpl extends GenericServiceImpl<Diagnosis> implements DiagnosisService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    /**
     * {@inheritDoc}
     */
    public List<Diagnosis> getDiagnosis() {
        if (Constants.DIAGNOSIS.isEmpty()) {
            Constants.DIAGNOSIS.addAll(diagnosisRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.DIAGNOSIS;
    }

}
