package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.CountryClassification;
import com.mdtlabs.coreplatform.common.service.GenericTenantService;

import java.util.List;

/**
 * <p>
 * This an interface class for country module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface CountryClassificationService extends GenericTenantService<CountryClassification> {

    /**
     * <p>
     * Retrieves list of Country Classification by country id.
     * </p>
     *
     * @param countryId {@link Long} long Id
     * @return List of CountryClassification  {@link List<CountryClassification>} list of entity
     */
    List<CountryClassification> getClassificationsByCountryId(Long countryId);
}
