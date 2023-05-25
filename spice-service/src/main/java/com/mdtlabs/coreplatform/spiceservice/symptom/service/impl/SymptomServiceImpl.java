package com.mdtlabs.coreplatform.spiceservice.symptom.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.symptom.repository.SymptomRepository;
import com.mdtlabs.coreplatform.spiceservice.symptom.service.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * SymptomServiceImpl class implements method for managing symptom.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class SymptomServiceImpl extends GenericServiceImpl<Symptom> implements SymptomService {

    @Autowired
    private SymptomRepository symptomRepository;

    /**
     * {@inheritDoc}
     */
    public List<Symptom> getSymptoms() {
        if (Constants.SYMPTOMS.isEmpty()) {
            Constants.SYMPTOMS.addAll(symptomRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.SYMPTOMS;
    }
}
