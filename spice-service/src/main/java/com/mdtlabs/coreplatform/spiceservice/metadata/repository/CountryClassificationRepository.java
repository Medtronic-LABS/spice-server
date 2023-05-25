package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.CountryClassification;
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
public interface CountryClassificationRepository extends GenericTenantRepository<CountryClassification> {

    /**
     * <p>
     * Gets Country Classification entity list with countryId field as input.
     * </p>
     *
     * @return {@link List<CountryClassification>} List of Country Classification entities.
     */
    List<CountryClassification> findByCountryIdAndIsDeletedFalse(Long countryId);

}
