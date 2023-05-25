package com.mdtlabs.coreplatform.spiceadminservice.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Country;

/**
 * <p>
 * CountryRepository is a Java interface for a Spring Data JPA repository for managing Country entities. It extends the
 * JpaRepository interface and provides methods for retrieving and manipulating Country entities from the database.
 * </p>
 *
 * @author Karthick M created on feb 09, 2023
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    String SELECT_BASE_QUERY = "select country from Country as country";
    String GET_ALL_COUNTRIES = SELECT_BASE_QUERY + " where country.isDeleted=false AND country.isActive = true";

    String GET_COUNTRIES_BY_NAME = SELECT_BASE_QUERY + " where lower(country.name) LIKE CONCAT('%',lower(:searchTerm),'%')"
            + " AND country.isDeleted=false AND country.isActive = true order by country.updatedBy";
    String GET_COUNTRIES_COUNT_BY_NAME = "select count(id) from Country "
            + "as country where lower(country.name) LIKE CONCAT('%',lower(:searchTerm),'%') "
            + "AND country.isDeleted=false AND country.isActive = true";

    /**
     * <p>
     * This method is used to get a country from the database based on countryCode and name.
     * </p>
     *
     * @param countryCode {@link Long} The country code associated with the countries that are being searched is given
     * @param name        {@link String} The country name for which the all the countries are being searched is given
     * @return {@link List<Country>} The list of countries for the given country code and name is retrieved
     * from the database and returned
     */
    List<Country> findByCountryCodeOrNameIgnoreCase(String countryCode, String name);

    /**
     * <p>
     * This method is used to get a page of countries from the database based on given pagination.
     * </p>
     *
     * @param pageable {@link Pageable} The pagination information that contains information such as the page number,
     *                 page size, sorting criteria, and more is given
     * @return {@link Page<Country>} A Page of non-deleted active countries with applied pagination is
     * retrieved and returned from the database
     */
    @Query(value = GET_ALL_COUNTRIES)
    Page<Country> getAllCountries(Pageable pageable);

    /**
     * <p>
     * This method is used to get a page of countries from the database based on given search term and pagination.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Country>} A Page of non-deleted active countries that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_COUNTRIES_BY_NAME)
    Page<Country> searchCountries(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm, Pageable pageable);

    /**
     * <p>
     * This method is used to get a count of all countries based on the given search term.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @return The count of countries of given search term is retrieved from the database and returned
     */
    @Query(value = GET_COUNTRIES_COUNT_BY_NAME)
    int getCountryCountByName(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm);

    /**
     * <p>
     * This method is used to get the country based on country ID and deletion status.
     * </p>
     *
     * @param countryId The ID of the country that need to be retrieved is given
     * @param isDeleted {@link Boolean} The boolean value that is used to filter the results of the query based
     *                  on whether the country has been marked as deleted or not is given
     * @return {@link Country} The country for the given country ID and deletion status is retrieved
     * from the database and returned
     */
    Country findByIdAndIsDeleted(long countryId, boolean isDeleted);

    /**
     * <p>
     * This method is used to get the count of non-deleted countries from the database.
     * </p>
     *
     * @return The count of non-deleted countries is retrieved from the database and returned
     */
    int countByIsDeletedFalse();

    /**
     * <p>
     * The list of countries for the given active status is retrieved from the database.
     * </p>
     *
     * @param isActive {@link Boolean} The boolean value that is used to filter the results of the query based
     *                 on whether the country has been marked as active or not is given
     * @return {@link List<Country>} The list of countries for the given active status is retrieved
     * from the database and returned
     */
    List<Country> findByIsActive(Boolean isActive);

    /**
     * <p>
     * This method is used to get non-deleted country by given tenant ID.
     * </p>
     *
     * @param tenantId {@link Long} The tenant ID of the country that need to be retrieved is given
     * @return {@link Country} The non-deleted country for the given tenant ID is retrieved
     * from the database and returned
     */
    Country findByTenantIdAndIsDeletedFalse(Long tenantId);

    /**
     * <p>
     * This method is used to get non-deleted country by given tenant ID and active status.
     * </p>
     *
     * @param tenantId {@link Long} The tenant ID of the country that need to be retrieved is given
     * @param isActive {@link Boolean} The boolean value that is used to filter the results of the query based
     *                 on whether the country has been marked as active or not is given
     * @return {@link Country} The non-deleted country for the given tenant ID and active status is retrieved
     * from the database and returned
     */
    Country findByTenantIdAndIsDeletedFalseAndIsActive(Long tenantId, boolean isActive);

    /**
     * <p>
     * This method is used to retrieve country by ID and tenant ID.
     * </p>
     *
     * @param id       {@link Long} The ID of the country that need to be retrieved is given
     * @param tenantId {@link Long} The tenant ID of the country that need to be retrieved is given
     * @return {@link Country} The country for given id and tenant id is retrieved
     * from the database
     */
    Country findByIdAndTenantIdAndIsDeletedFalse(Long id, Long tenantId);
}
