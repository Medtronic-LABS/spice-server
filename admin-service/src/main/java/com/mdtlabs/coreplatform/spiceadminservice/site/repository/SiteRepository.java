package com.mdtlabs.coreplatform.spiceadminservice.site.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Site;

/**
 * <p>
 * SiteRepository a Java interface that extends the JpaRepository interface and defines methods for accessing and
 * manipulating data in a database table for Site entities.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    String SELECT_BASE_QUERY = "select site from Site as site";
    String GET_SITE_LIST = SELECT_BASE_QUERY + " where (:searchTerm is null "
            + "or lower(site.name) LIKE CONCAT('%',lower(:searchTerm),'%')) and (:countryId is null or "
            + "site.countryId=:countryId) and (:operatingUnitId is null or site.operatingUnit.id=:operatingUnitId) "
            + "and (:accountId is null or site.accountId=:accountId) AND site.isDeleted=false AND site.isActive=true";

    String GET_BY_OPERATING_UNIT_AND_IS_DELETED_FALSE = SELECT_BASE_QUERY + " where"
            + " site.isDeleted=false AND site.operatingUnit.id=:operatingUnitId";


    String GET_SITE = SELECT_BASE_QUERY + " where (:countryId is null or "
            + "site.countryId=:countryId) AND (:accountId is null or site.accountId=:accountId) AND (:operatingUnitId "
            + "is null or site.operatingUnit.id =:operatingUnitId) AND site.isDeleted=false AND site.isActive=:isActive";

    String COUNT_BY_ACCOUNT_IDS = "select site.accountId as accountId, count(site.id) as count "
            + "from Site as site where site.accountId in (:accountIds) AND site.isDeleted=false AND "
            + "site.isActive=true group by site.accountId";

    String GET_SITES_BY_COUNTRY_TENANT = "select site from Site "
            + "as site where lower(site.name)"
            + " LIKE CONCAT('%',lower(:searchTerm),'%') AND site.isDeleted=false AND site.isActive=true AND "
            + "site.tenantId!=:tenantId AND (:countryId is null or site.countryId=:countryId)";

    String COUNT_BY_OU_ID = "SELECT site.operatingUnit.id as operatingUnitId, COUNT(site.id) AS count "
            + "FROM Site as site WHERE site.operatingUnit.id in :operatingUnitIds AND site.isDeleted=false AND "
            + "site.isActive=true GROUP BY site.operatingUnit.id";

    String COUNT_BY_COUNTRY_ID = "SELECT site.countryId as countryId, COUNT(site.id) AS count "
            + "FROM Site as site WHERE site.countryId in :countryIds AND site.isDeleted=false AND "
            + "site.isActive=true GROUP BY site.countryId";

    /**
     * <p>
     * This method is used to retrieve list of sites from the database based on the provided list of tenant IDs.
     * </p>
     *
     * @param tenants {@link List<Long>} The list of tenant IDs associated with the that need to retrieved
     *                is given
     * @return {@link List<Site>} The list of non-deleted sites for the given list of tenant IDs
     * is retrieved from the database
     */
    List<Site> findByIsDeletedFalseAndTenantIdIn(List<Long> tenants);

    /**
     * <p>
     * This method is used to retrieve a site from the database based on its id.
     * </p>
     *
     * @param siteId {@link Long} The ID for which the site is being searched is given
     * @return {@link Site} The non-deleted site for the given ID is retrieved and returned from the database
     */
    Site findByIdAndIsDeletedFalse(Long siteId);

    /**
     * <p>
     * This method is used to get page of non-deleted and active sites for the given search criteria.
     * </p>
     *
     * @param searchTerm      {@link String} The term or keyword that is being searched for in the query to
     *                        filter the results is given
     * @param accountId       {@link Long} The account ID for which the site is being searched is given
     * @param operatingUnitId {@link Long} The operating unit ID for which the site is being searched is given
     * @param countryId       {@link Long} The ID of the country associated with the sites that
     *                        are being searched is given
     * @param pageable        {@link Pageable} The pagination information that contains information such as the page number,
     *                        page size, sorting criteria, and more is given
     * @return {@link Page<Site>} A Page of non-deleted and active sites that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_SITE_LIST)
    Page<Site> getAllSite(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                          @Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.ACCOUNT_ID) Long accountId,
                          @Param(Constants.OPERATING_UNIT_ID) Long operatingUnitId, Pageable pageable);

    /**
     * <p>
     * This method is used to find list of non-deleted sites by given operating unit ID.
     * </p>
     *
     * @param operatingUnitId {@link Long} The operating unit ID for which the site is being searched is given
     * @return {@link List<Site>} The list of non-deleted sites for the given operating unit ID
     * is retrieved from the database
     */
    @Query(value = GET_BY_OPERATING_UNIT_AND_IS_DELETED_FALSE)
    List<Site> getByOperatingUnitAndIsDeletedFalse(@Param(Constants.OPERATING_UNIT_ID) Long operatingUnitId);

    /**
     * <p>
     * This method is used to get page of non-deleted and active sites for the given search criteria.
     * </p>
     *
     * @param accountId     {@link Long} The account ID for which the site is being searched is given
     * @param operatingUnit {@link Long} The operating unit ID for which the site is being searched is given
     * @param countryId     {@link Long} The ID of the country associated with the sites that
     *                      are being searched is given
     * @param isActive      The boolean value that is used to filter the results of the query based
     *                      on whether the sites have been marked as active or not is given
     * @return {@link List} The list of sites those are not deleted is returned for the
     * given search criteria like country ID, account ID, operating unit ID and active status
     */
    @Query(value = GET_SITE)
    List<Site> findSite(@Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.ACCOUNT_ID) Long accountId,
                        @Param(Constants.OPERATING_UNIT_ID) Long operatingUnit, @Param(Constants.IS_ACTIVE) boolean isActive);

    /**
     * <p>
     * This method is used to get a list of map containing operating unit IDs with corresponding count of sites that
     * is searched using the given account IDs.
     * </p>
     *
     * @param accountIds {@link List<Long>} The list of account IDs associated with the sites that need to counted
     *                   is given
     * @return {@link List<Map>} A list of map containing key as account IDs and value as count of sites
     * for the corresponding account IDs provided is retrieved and returned from the database
     */
    @Query(value = COUNT_BY_ACCOUNT_IDS)
    List<Map<String, Object>> getCountByAccountIds(@Param(Constants.ACCOUNT_IDS) List<Long> accountIds);

    /**
     * <p>
     * This method is used to get an non-deleted site based on its ID and tenant ID.
     * </p>
     *
     * @param id       {@link Long} The ID for which the site is being searched is given
     * @param tenantId {@link Long} The tenant ID for which the site is being searched is given
     * @return {@link Site} The non-deleted site for the given ID and tenant ID
     * is retrieved from the database
     */
    Site findByIdAndIsDeletedFalseAndTenantId(Long id, Long tenantId);

    /**
     * <p>
     * This method is used to retrieve set of sites from the database based on the provided list of IDs.
     * </p>
     *
     * @param ids {@link Set} The set of IDs associated with the sites that need to retrieved is given
     * @return {@link Set} The set of non-deleted sites for the given list of IDs is retrieved from the database
     */
    Set<Site> findByIdInAndIsDeletedFalse(Set<Long> ids);

    /**
     * <p>
     * Finds a site based on its isActive status and ID.
     * </p>
     *
     * @param id             The ID for which the site is being searched is given
     * @param isActiveStatus The boolean value that is used to filter the results of the query based
     *                       on whether the sites have been marked as active or not is given
     * @return {@link Site} The non-deleted site for the given ID with specified active status
     * is retrieved from the database
     */
    Site findByIdAndIsActive(long id, boolean isActiveStatus);

    /**
     * <p>
     * This method is used to get a list of map containing operating unit IDs with corresponding count of sites that
     * is searched using the given operating unit IDs.
     * </p>
     *
     * @param operatingUnitIds {@link List<Long>} The list of operating unit IDs associated with the sites
     *                         that need to counted is given
     * @return {@link List<Map>} A list of map containing key as operating unit IDs and value as count of sites
     * for the corresponding operating unit IDs provided is retrieved and returned from the database
     */
    @Query(value = COUNT_BY_OU_ID)
    List<Map<String, Object>> getSiteCountByOperatingUnitIds(
            @Param(Constants.OPERATING_UNIT_IDS) List<Long> operatingUnitIds);

    /**
     * <p>
     * This method is used to get a list of map containing country IDs with corresponding count of sites that
     * is searched using the given country IDs.
     * </p>
     *
     * @param countryIds  {@link List<Long>} The list of country IDs associated with the sites that need to counted
     *                    is given
     * @return {@link List<Map>} A list of map containing key as country IDs and value as count of sites
     * for the corresponding country IDs provided is retrieved and returned from the database
     */
    @Query(value = COUNT_BY_COUNTRY_ID)
    List<Map<String, Object>> getSiteCountByCountryIds(List<Long> countryIds);

    /**
     * <p>
     * This method is used to find a list of non-deleted sites by given Id and active status.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param tenantId   {@link Long} The tenant ID for which the site is being searched is given
     * @param countryId  {@link Long} The ID of the country associated with the sites that
     *                   are being searched is given
     * @return {@link List<Site>} The list of non-deleted sites for the given country ID and tenant ID
     * is retrieved from the database
     */
    @Query(value = GET_SITES_BY_COUNTRY_TENANT)
    List<Site> getSiteByCountryAndTenant(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm, Long tenantId,
                                         @Param(Constants.COUNTRY_ID) Long countryId);

    /**
     * <p>
     * This method is used to check if a site exists in a database table based on the name which is not deleted.
     * </p>
     *
     * @param name {@link String} The name of the site to be checked is given
     * @return A boolean value is returned by checking if non-deleted site with the
     * given name exists in the database, ignoring case sensitivity
     */
    boolean existsByNameIgnoreCaseAndIsDeletedFalse(String name);

    /**
     * <p>
     * This method is used to check if a site exists in a database table based on the given name and not having given
     * ID which is not deleted.
     * </p>
     *
     * @param name {@link String} The name of the site to be checked is given
     * @param id   {@link Long} The id of the site to be checked is given
     * @return A boolean value is returned by checking if non-deleted site with the
     * given name and not in ID exists in the database, ignoring case sensitivity
     */
    boolean existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(String name, Long id);
}
