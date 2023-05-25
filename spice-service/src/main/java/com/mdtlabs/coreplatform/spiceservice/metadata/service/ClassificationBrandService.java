package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.ClassificationBrand;
import com.mdtlabs.coreplatform.common.service.GenericTenantService;

import java.util.List;

/**
 * <p>
 * This an interface class for classification brand module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface ClassificationBrandService extends GenericTenantService<ClassificationBrand> {

    /**
     * This function retrieves a list of ClassificationBrand objects based on a given country ID and
     * classification ID.
     *
     * @param countryId        {@link Long} The ID of the country for which you want to retrieve the ClassificationBrand
     *                         objects.
     * @param classificationId {@link Long} The ID of the classification for which you want to retrieve the brands.
     * @return {@link List<ClassificationBrand>} The method `getByCountryAndClassificationId` is returning a list of
     * `ClassificationBrand` objects that match the given `countryId` and `classificationId`.
     */
    List<ClassificationBrand> getByCountryAndClassificationId(Long countryId, Long classificationId);

}
