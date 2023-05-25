package com.mdtlabs.coreplatform.spiceadminservice.data.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.Subcounty;

/**
 * <p>
 * This is a Java interface that extends the JpaRepository interface, which provides database connection and CRUD
 * operations for the Subcounty entity.
 * </p>
 *
 * @author Karthick M created on feb 09, 2023
 */
@Repository
public interface SubCountyRepository extends JpaRepository<Subcounty, Long> {

    String GET_ALL_SUBCOUNTIES = "select subcounty from Subcounty"
            + " as subcounty where subcounty.countryId=:countryId AND subcounty.countyId=:countyId "
            + "AND subcounty.isDeleted=false";

    /**
     * <p>
     * This method is used to find the sub county from the database based on given name.
     * </p>
     *
     * @param name {@link String} The sub county name for which the sub county is being searched is given
     * @return {@link Subcounty} The sub county for the given name is retrieved from the database and returned
     */
    Subcounty findByName(String name);

    /**
     * <p>
     * This method is used to find the sub county from the database based on given ID and deletion status.
     * </p>
     *
     * @param id        The ID of the sub county that need to be deleted retrieve is given
     * @param isDeleted {@link Boolean} The boolean value that is used to filter the results of the query based
     *                  on whether the sub county has been marked as deleted or not is given
     * @return {@link Subcounty} The sub county for the given ID and deletion status is retrieved
     * from the database and returned
     */
    Subcounty findByIdAndIsDeleted(long id, boolean isDeleted);

    /**
     * <p>
     * This method is used to find the sub county from the database based on given country ID, county ID and name.
     * </p>
     *
     * @param countryId The country ID for which the sub county is being searched is given
     * @param countyId  The county ID for which the sub county is being searched is given
     * @param name      {@link String} The sub county name for which the sub county is being searched is given
     * @return {@link Subcounty} The sub county for the given country ID, county ID and name is retrieved
     * from the database and returned
     */
    Subcounty findByCountryIdAndCountyIdAndName(@NotNull long countryId, @NotNull long countyId,
                                                @NotNull String name);

    /**
     * <p>
     * This method is to get all sub counties from the database based on given country ID and county ID.
     * </p>
     *
     * @param countryId The country ID for which the sub counties are being searched is given
     * @param countyId  The county ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of sub counties for the given country ID and county ID is retrieved from
     * the database and returned
     */
    @Query(value = GET_ALL_SUBCOUNTIES)
    List<Subcounty> getAllSubCounty(@Param("countryId") long countryId, @Param("countyId") long countyId);

    /**
     * <p>
     * This method is to get the list of sub counties from the database based on given country ID.
     * </p>
     *
     * @param countryId {@link Long} The country ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of non-deleted sub counties for given country ID is retrieved from
     * the database and returned
     */
    List<Subcounty> findByCountryId(Long countryId);

    /**
     * <p>
     * This method is to get the list of non-deleted sub counties from the database based on given county ID.
     * </p>
     *
     * @param countyId {@link Long} The county ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of non-deleted sub counties for given county ID is retrieved from
     * the database and returned
     */
    List<Subcounty> findByCountyIdAndIsDeletedFalse(Long countyId);
}
