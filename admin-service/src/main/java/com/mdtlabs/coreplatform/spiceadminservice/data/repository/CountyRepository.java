package com.mdtlabs.coreplatform.spiceadminservice.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;

/**
 * <p>
 * CountyRepository is a Java interface for a repository that helps maintain the DB connection to perform
 * operations on the County entity.
 * </p>
 *
 * @author Niraimathi S created on feb 09, 2023
 */
public interface CountyRepository extends JpaRepository<County, Long> {

    /**
     * <p>
     * This method is used to get the county from the database based on given country ID and name.
     * </p>
     *
     * @param countryId {@link Long} The country ID associated with the counties that are being searched is given
     * @param name      {@link String} The county name for which the all the counties are being searched is given
     * @return {@link County} The county for the given country ID and name is retrieved
     * from the database and returned
     */
    County findByCountryIdAndName(Long countryId, String name);

    /**
     * <p>
     * This method is used to get the county from the database based on given ID and deletion status.
     * </p>
     *
     * @param id The ID of the county that need to be retrieve is given
     * @param b  The boolean value that is used to filter the results of the query based
     *           on whether the county has been marked as deleted or not is given
     * @return {@link County} The county for the given ID and deletion status is retrieved
     * from the database and returned
     */
    County findByIdAndIsDeleted(long id, boolean b);

    /**
     * <p>
     * This method is used to get the list of counties from the database based on given country ID.
     * </p>
     *
     * @param countryId The country ID associated with the counties that are being searched is given
     * @return {@link List<Subcounty>} The list of counties for given county ID is retrieved from
     * the database and returned
     */
    List<County> findByCountryId(long countryId);
}
