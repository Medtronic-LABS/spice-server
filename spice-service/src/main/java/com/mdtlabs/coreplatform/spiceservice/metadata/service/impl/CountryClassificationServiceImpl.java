package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.model.entity.spice.CountryClassification;
import com.mdtlabs.coreplatform.common.service.impl.GenericTenantServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.CountryClassificationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CountryClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for country classification module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class CountryClassificationServiceImpl extends GenericTenantServiceImpl<CountryClassification>
        implements CountryClassificationService {

    @Autowired
    private CountryClassificationRepository countryClassificationRepository;

    /**
     * {@inheritDoc}
     */
    public List<CountryClassification> getClassificationsByCountryId(Long countryId) {
        return countryClassificationRepository.findByCountryIdAndIsDeletedFalse(countryId);
    }
}
