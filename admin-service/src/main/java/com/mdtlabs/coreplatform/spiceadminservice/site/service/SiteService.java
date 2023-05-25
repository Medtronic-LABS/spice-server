package com.mdtlabs.coreplatform.spiceadminservice.site.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.User;

/**
 * <p>
 * SiteService is defining an interface called SiteService which contains several methods for managing sites. These
 * methods include adding, updating, and retrieving sites, activating or deactivating sites, retrieving site details,
 * adding and updating site admins, and getting lists of sites and cities based on various search criteria.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface SiteService {

    /**
     * <p>
     * This method is used to create a new site using the provided site details.
     * </p>
     *
     * @param site {@link Site} The site that contains necessary information
     *             to create site is given
     * @return {@link Site} The site is created using the provided site details and then returned
     */
    Site addSite(Site site);

    /**
     * <p>
     * This method is used to get list of sites using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of sites is given
     * @return {@link ResponseListDTO} The retrieved list of sites and total count for given request is returned
     */
    ResponseListDTO getSites(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to update a existing site using the provided site details.
     * </p>
     *
     * @param site {@link Site} The site that contains necessary information
     *             to update site is given
     * @return {@link Site} The site is updated using the provided site details and then returned
     */
    Site updateSite(Site site);

    /**
     * <p>
     * This method is used to activate or deactivate a site based on given ID and active status.
     * </p>
     *
     * @param id             The ID for which the site is being searched is given
     * @param isActiveStatus The boolean value that is used to filter the results of the query based
     *                       on whether the sites have been marked as active or not is given
     * @return {@link Site} The  activated or deactivated site for the given ID is returned
     */
    Site activateDeactivateSite(long id, boolean isActiveStatus);

    /**
     * <p>
     * This method is used to retrieve a list of sites based on the given list of tenant IDs.
     * </p>
     *
     * @param tenants {@link List<Long>} The list of tenantIds that belongs to the list of sites that
     *                need to be retrieved is given
     * @return {@link List<Site>} The list of sites for the given list of tenant IDs is returned
     */
    List<Site> getSitesByTenantIds(List<Long> tenants);

    /**
     * <p>
     * This method is used to get list of sites for the given operating unit ID.
     * </p>
     *
     * @param operatingUnitId {@link Long} The search request contains necessary information
     *                        to get the list of sites is given
     * @return {@link List<Site>} The list of sites for the given operating unit ID is retrieved and retrieved
     */
    List<Site> getSitesByOperatingUnitId(Long operatingUnitId);

    /**
     * <p>
     * This method is used to retrieve site by its ID.
     * </p>
     *
     * @param siteId {@link Long} The id of the site that need to be retrieved is given
     * @return {@link Site} The site for the given id is returned
     */
    Site getSiteById(Long siteId);

    /**
     * <p>
     * To activate or deactivate list of sites based on its tenantIds.
     * </p>
     *
     * @param accountId       {@link Long} The account ID for which the site is being searched is given
     * @param operatingUnitId {@link Long} The operating unit ID for which the site is being searched is given
     * @param countryId       {@link Long} The ID of the country associated with the sites that
     *                        are being searched is given
     * @param isActive        The boolean value that is used to filter the results of the query based
     *                        on whether the sites have been marked as active or not is given
     * @return The list of activated or deactivated sites for given search criteria is returned.
     */
    List<Long> activateOrDeactivateSites(Long countryId, Long accountId, Long operatingUnitId, boolean isActive);

    /**
     * <p>
     * This method is used to retrieve set of sites based on the provided list of IDs.
     * </p>
     *
     * @param ids {@link Set} The set of IDs associated with the sites that need to retrieved is given
     * @return {@link Set} The set of sites for the given list of IDs is retrieved from the database
     */
    Set<Site> getSiteByIds(Set<Long> ids);

    /**
     * <p>
     * This method is used to add a site admin using the provided details.
     * </p>
     *
     * @param user {@link SiteUserDTO} The site user need to be added is given
     * @return {@link User} The user is added for given user details and then returned
     */
    User addSiteAdmin(SiteUserDTO user);

    /**
     * <p>
     * This method is used to update a site admin using the provided details.
     * </p>
     *
     * @param user {@link SiteUserDTO} The site user need to be updated is given
     * @return {@link User} The user is updated for given user details and then returned
     */
    User updateSiteAdmin(SiteUserDTO user);

    /**
     * <p>
     * This method is used to delete an site admin for the given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The parameters required to delete the
     *                   site admin is given
     * @return {@link Boolean} The site user for the given request is
     * deleted and the corresponding boolean value is returned
     */
    Boolean deleteSiteAdmin(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to get a site list DTO using given request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The parameters required to get the
     *                   site list DTO is given
     * @return {@link List<Site>} The site list DTO for the given request
     * is returned
     */
    SiteDetailsDTO getSiteDetails(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to get a list of map containing operating unit IDs with corresponding count of sites that
     * is searched using the given account IDs.
     * </p>
     *
     * @param accountIds {@link List<Long>} The list of account IDs associated with the sites that need to counted
     *                   is given
     * @return {@link List<Map>} A list of map containing key as account IDs and value as count of sites
     * for the corresponding account IDs provided is returned
     */
    Map<Long, Long> getCountByAccountIds(List<Long> accountIds);
    
    /**
     * <p>
     * This method is used to get a list of sites using given request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The parameters required to get the
     *                   list of sites is given
     * @return {@link List<Site>} The list of sites for the given request
     * is returned
     */
    List<Site> getSiteByCountry(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to get a list of map containing operating unit IDs with corresponding count of sites that
     * is searched using the given operating unit IDs.
     * </p>
     *
     * @param operatingUnitIds {@link List<Long>} The list of operating unit IDs associated with the sites
     *                         that need to counted is given
     * @return {@link Map} A map containing key as operating unit IDs and value as count of sites
     * for the corresponding operating unit IDs provided is returned
     */
    Map<Long, Long> getSiteCountByOperatingUnitIds(List<Long> operatingUnitIds);

    /**
     * <p>
     * This method is used to get a list of map containing country IDs with corresponding count of sites that
     * is searched using the given country IDs.
     * </p>
     *
     * @param countryIds {@link List<Long>} The list of country IDs associated with the sites that need to counted
     *                   is given
     * @return {@link Map} A map containing key as country IDs and value as count of sites
     * for the corresponding country IDs provided is returned
     */
    Map<Long, Long> getSiteCountByCountryIds(List<Long> countryIds);

    /**
     * <p>
     * This method is used to get list of cities using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information
     *                   to get the list of cities is given
     * @return {@link List<Map>} The retrieved list of cities for the given request is returned
     */
    List<Map<String, String>> getCitiesList(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to get map containing city coordinates using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information
     *                   to get the map containing city coordinates is given
     * @return {@link Map} The retrieved map containing the city coordinates for the given request is returned
     */
    Map<String, Object> getCityCoordinates(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to get a map of all site IDs and names.
     * </p>
     *
     * @return {@link Map} A map containing site IDs as keys and site names as values is being returned
     */
    Map<Long, String> getAllSiteIdAndName();
}
