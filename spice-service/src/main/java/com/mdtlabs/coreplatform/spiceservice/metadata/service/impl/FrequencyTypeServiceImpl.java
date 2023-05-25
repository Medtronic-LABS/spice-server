package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.FrequencyType;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.FrequencyTypeRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.FrequencyTypeService;

/**
 * <p>
 * This service class contain all the business logic for FrequencyType module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class FrequencyTypeServiceImpl extends GenericServiceImpl<FrequencyType> implements FrequencyTypeService {

    @Autowired
    private FrequencyTypeRepository frequencyTypeRepository;

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getFrequencyTypesByCultureId(Long cultureId) {
        if (Constants.FREQUENCY_TYPE.isEmpty()) {
            Constants.FREQUENCY_TYPE.addAll(frequencyTypeRepository.findByIsDeletedFalse());
        }
        Map<String, String> frequencyTypeMap = new HashMap<>();
        Constants.FREQUENCY_TYPE.forEach( frequencyType -> {
            if (cultureId.equals(frequencyType.getCultureId())) {
                frequencyTypeMap.put(frequencyType.getName(), frequencyType.getCultureValue());
            }
        });
        return frequencyTypeMap;
    }
    
}
