package com.mdtlabs.coreplatform.spiceadminservice.operatingunit.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;

/**
 * <p>
 * OperatingUnitRepository is a Java interface that extends the JpaRepository interface and defines methods
 * for accessing and manipulating data in the Operatingunit table of a database.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface OperatingUnitRepository extends JpaRepository<Operatingunit, Long> {

    String SELECT_BASE_QUERY = "select operatingUnit from Operatingunit as operatingUnit";
    String GET_ALL_OPERATING_UNITS = SELECT_BASE_QUERY + " where lower(operatingUnit.name) "
            + " LIKE CONCAT('%',lower(:searchTerm),'%') AND operatingUnit.isDeleted=false AND"
            + "(:countryId is null or operatingUnit.countryId=:countryId) AND (:accountId is null or "
            + "operatingUnit.account.id=:accountId) AND operatingUnit.isActive=true ";

    String GET_ALL_OPERATING_UNITS_BY_TENANTS = SELECT_BASE_QUERY + " where (:searchTerm is null or lower(operatingUnit.name) "
            + " LIKE CONCAT('%',lower(:searchTerm),'%')) AND operatingUnit.isDeleted=false AND "
            + "(:countryId is null or operatingUnit.countryId=:countryId) AND (:accountId is null or "
            + "operatingUnit.account.id=:accountId) AND operatingUnit.isActive=true";

    String GET_BY_ACCOUNT_ID_AND_COUNTRY_ID = SELECT_BASE_QUERY + " where operatingUnit.isDeleted=false AND "
            + " (:countryId is null or operatingUnit.countryId=:countryId) AND (:accountId "
            + "is null or operatingUnit.account.id=:accountId) AND operatingUnit.isActive=:isActive";

    String COUNT_ON_COUNTRY_ID = "SELECT operatingUnit.countryId as countryId,COUNT(operatingUnit.id) "
            + "AS count FROM Operatingunit as operatingUnit WHERE operatingUnit.countryId in :countryIds AND "
            + "operatingUnit.isDeleted=false AND operatingUnit.isActive=true GROUP BY operatingUnit.countryId";

    String COUNT_ON_ACCOUNT_ID = "SELECT operatingUnit.account.id as accountId, COUNT(operatingUnit.id) "
            + "AS count FROM Operatingunit as operatingUnit WHERE operatingUnit.account.id in :accountIds AND "
            + "operatingUnit.isDeleted=false AND operatingUnit.isActive=true GROUP BY operatingUnit.account.id";

    /**
     * <p>
     * This method is used to get a list of operating units based on given name.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Operatingunit>} A Page of operating units that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ALL_OPERATING_UNITS)
    Page<Operatingunit> findOperatingUnits(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                           @Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.ACCOUNT_ID) Long accountId, Pageable pageable);

    /**
     * <p>
     * This method is used to find a list of operating units based on country Id or account Id.
     * </p>
     *
     * @param countryId {@link Long} The ID of the country for which the operating units
     *                  are being searched is given
     * @param accountId {@link Long} The ID of the country for which the operating units
     *                  are being searched is given
     * @param isActive  The boolean value that is used to filter the results of the query based
     *                  on whether the operating units have been marked as active or not is given
     * @return {@link List} The list of operating units those are not deleted is returned for the
     * given search criteria like country ID, account ID and active status
     */
    @Query(value = GET_BY_ACCOUNT_ID_AND_COUNTRY_ID)
    List<Operatingunit> findByCountryIdAndAccountIdAndIsActive(@Param(Constants.COUNTRY_ID) Long countryId,
                                                               @Param("accountId") Long accountId, @Param(Constants.IS_ACTIVE) boolean isActive);

    /**
     * <p>
     * This method is used to get an non-deleted operating unit based on its Id,tenantId and isActive status.
     * </p>
     *
     * @param id        {@link Long} The ID for which the operating unit is being searched is given
     * @param isActive  {@link Boolean} The boolean value that is used to filter the results of the query based
     *                  on whether the operating unit has been marked as active or not is given
     * @param isDeleted {@link Boolean} The boolean value that is used to filter the results of the query based
     *                  on whether the operating unit has been marked as deleted or not is given
     * @param tenantId  {@link Long} The tenant ID for which the operating unit is being searched is given
     * @return {@link Operatingunit} The non-deleted operating unit for the given ID and tenant ID with
     * specified active status is retrieved from the database
     */
    Operatingunit findByIdAndIsActiveAndIsDeletedAndTenantId(Long id, Boolean isActive, Boolean isDeleted,
                                                             Long tenantId);

    /**
     * <p>
     * This method is used to find an non-deleted operating unit by given Id and active status.
     * </p>
     *
     * @param id       {@link Long} The ID for which the operating unit is being searched is given
     * @param isActive {@link Boolean} The boolean value that is used to filter the results of the query based
     *                 on whether the operating unit has been marked as active or not is given
     * @return {@link Operatingunit} The non-deleted operating unit for the given ID with
     * specified active status is retrieved from the database
     */
    Operatingunit findByIdAndIsDeletedFalseAndIsActive(long id, boolean isActive);

    /**
     * <p>
     * This method is used to get operating unit by its name that has not been deleted.
     * </p>
     *
     * @param name {@link String} The name of the operating unit to be searched is given
     * @return {@link Operatingunit} The non-deleted operating unit for the given name (ignoring case) with
     * specified active status is retrieved from the database
     */
    Operatingunit findByNameIgnoreCaseAndIsDeletedFalse(String name);

    /**
     * <p>
     * This method is used to get a list of operating units based search term, country ID and account ID with the given
     * pagination.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param countryId  {@link Long} The ID of the country associated with the operating unit that
     *                   are being searched is given
     * @param accountId  {@link Long} The ID of the account with the operating unit that
     *                   are being searched is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Operatingunit>} A Page of operating units that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ALL_OPERATING_UNITS_BY_TENANTS)
    Page<Operatingunit> getOperatingUnits(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                          @Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.ACCOUNT_ID) Long accountId, Pageable pageable);

    /**
     * <p>
     * This method is used to get a list of map containing country IDs with corresponding count of operating units that
     * is searched using the given country IDs.
     * </p>
     *
     * @param countryIds {@link List} The list of country IDs associated with the operating units that
     *                   are being searched is given
     * @return {@link List<Map>} A list of map containing key as country IDs and value as count of operating units
     * for the corresponding country IDs provided is retrieved and returned from the database
     */
    @Query(value = COUNT_ON_COUNTRY_ID)
    List<Map<String, Object>> getOperatingUnitCountByCountryIds(@Param(Constants.COUNTRY_IDS) List<Long> countryIds);

    /**
     * <p>
     * This method is used to get a list of map containing account IDs with corresponding count of operating units that
     * is searched using the given account IDs.
     * </p>
     *
     * @param accountIds {@link List} The list of account IDs associated with the operating units that
     *                   are being searched is given
     * @param isActive   {@link Boolean} The boolean value that is used to filter the results of the query based
     *                   on whether the operating unit has been marked as active or not is given
     * @return {@link List<Map>} A list of map containing key as account IDs and value as count of operating units
     * for the corresponding account IDs provided is retrieved and returned from the database
     */
    @Query(value = COUNT_ON_ACCOUNT_ID)
    List<Map<String, Object>> getOperatingUnitCountByAccountIds(@Param(Constants.ACCOUNT_IDS) List<Long> accountIds);

    /**
     * <p>
     * This method is used to check the availability of the operating unit by its name and ID which
     * has not been deleted.
     * </p>
     *
     * @param name {@link String} The name of the operating unit to be searched is given
     * @param id   {@link Long} The ID for which the operating unit is being searched is given
     * @return True is returned if the operating unit for given name and ID is already available
     * in the database otherwise false
     */
    boolean existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(String name, Long id);
}
