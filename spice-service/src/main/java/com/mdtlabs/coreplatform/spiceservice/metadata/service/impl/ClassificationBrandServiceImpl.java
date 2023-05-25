package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.model.entity.spice.ClassificationBrand;
import com.mdtlabs.coreplatform.common.service.impl.GenericTenantServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ClassificationBrandRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ClassificationBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for classification brand module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class ClassificationBrandServiceImpl extends GenericTenantServiceImpl<ClassificationBrand>
        implements ClassificationBrandService {

    @Autowired
    private ClassificationBrandRepository classificationBrandRepository;

    /**
     * {@inheritDoc}
     */
    public List<ClassificationBrand> getByCountryAndClassificationId(Long countryId, Long classificationId) {
        return classificationBrandRepository
                .findByCountryIdAndClassificationIdAndIsDeletedFalse(countryId, classificationId);
    }

}
