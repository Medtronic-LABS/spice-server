package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.ClassificationBrand;
import com.mdtlabs.coreplatform.common.repository.GenericTenantRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface ClassificationBrandRepository extends GenericTenantRepository<ClassificationBrand> {

    /**
     * <p>
     * This function finds a list of ClassificationBrand objects by country ID, classification ID, and
     * isDeleted flag set to false.
     * </p>
     *
     * @param countryId        {@link Long} A Long value representing the ID of the country for which the search is being
     *                         performed.
     * @param classificationId {@linnk Long} The ID of the classification for which we want to find the brands.
     * @return {@link List<ClassificationBrand>} The method is returning a list of objects of type "ClassificationBrand" that match the
     * given "countryId" and "classificationId" and have "isDeleted" property set to false.
     */
    List<ClassificationBrand> findByCountryIdAndClassificationIdAndIsDeletedFalse(Long countryId,
                                                                                  Long classificationId);

}
