package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComorbidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for comorbidity module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class ComorbidityServiceImpl extends GenericServiceImpl<Comorbidity> implements ComorbidityService {

    @Autowired
    ComorbidityRepository comorbidityRepository;

    /**
     * {@inheritDoc}
     */

    public List<Comorbidity> getComorbidityList() {
        if (Constants.COMORBIDITIES.isEmpty()) {
            Constants.COMORBIDITIES.addAll(comorbidityRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.COMORBIDITIES;
    }

}
