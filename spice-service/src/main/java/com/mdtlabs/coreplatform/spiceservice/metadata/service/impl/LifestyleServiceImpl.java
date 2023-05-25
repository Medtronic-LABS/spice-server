package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.LifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.LifestyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for lifestyle module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class LifestyleServiceImpl extends GenericServiceImpl<Lifestyle> implements LifestyleService {

    @Autowired
    private LifestyleRepository lifestyleRepository;

    /**
     * {@inheritDoc}
     */
    public List<Lifestyle> getLifestyles() {
        if (Constants.LIFESTYLES.isEmpty()) {
            Constants.LIFESTYLES.addAll(lifestyleRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.LIFESTYLES;
    }

    /**
     * {@inheritDoc}
     */
    public List<Lifestyle> getLifestylesByIds(List<Long> ids) {
        if (Constants.LIFESTYLES.isEmpty()) {
            Constants.LIFESTYLES.addAll(lifestyleRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.LIFESTYLES.stream().filter(lifestyle -> ids.contains(lifestyle.getId())).toList();
    }

}
