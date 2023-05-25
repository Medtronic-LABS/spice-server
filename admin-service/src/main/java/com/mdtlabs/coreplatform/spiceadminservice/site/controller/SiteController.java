package com.mdtlabs.coreplatform.spiceadminservice.site.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;

/**
 * <p>
 * SiteController class that defines a REST API for managing sites, including creating, updating, and retrieving site
 * information, as well as managing site administrators and retrieving lists of sites based on various criteria.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@RestController
@RequestMapping("/site")
@Validated
public class SiteController {

    @Autowired
    private SiteService siteService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create a new site using the site Dto.
     * </p>
     *
     * @param siteDto {@link SiteDTO} The site DTO that contains necessary information
     *                to create site is given
     * @return {@link Site} The site is created for the given site details and then returned
     */
    @PostMapping("/create")
    public Site addSite(@RequestBody SiteDTO siteDto) {
        return siteService.addSite(modelMapper.map(siteDto, Site.class));
    }

    /**
     * <p>
     * This method is used to update a existing site using the site Dto.
     * </p>
     *
     * @param siteDto {@link SiteDTO} The site DTO that contains necessary information
     *                to update site is given
     * @return {@link SuccessResponse<Site>} Returns a success message and status with the updated site
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<Site> updateSite(@Valid @RequestBody SiteDTO siteDto) {
        return new SuccessResponse<>(SuccessCode.SITE_UPDATE,
                siteService.updateSite(modelMapper.map(siteDto, Site.class)), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of cities using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information
     *                   to get the list of cities is given
     * @return {@link List<Map>} The retrieved list of cities for the given request is returned
     */
    @PostMapping("/list-cities")
    public List<Map<String, String>> getCitiesList(@RequestBody RequestDTO requestDto) {
        return siteService.getCitiesList(requestDto);
    }

    /**
     * <p>
     * This method is used to get map containing city coordinates using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information
     *                   to get the map containing city coordinates is given
     * @return {@link Map} The retrieved map containing the city coordinates for the given request is returned
     */
    @PostMapping("/get-city-coordinates")
    public Map<String, Object> getCityCoordinates(@RequestBody RequestDTO requestDto) {
        return siteService.getCityCoordinates(requestDto);
    }

    /**
     * <p>
     * This method is used to get list of sites using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of sites is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of sites and total count
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<List<SiteListDTO>> getSiteList(@RequestBody RequestDTO requestDto) {
        ResponseListDTO response = siteService.getSites(requestDto);
        return new SuccessResponse(SuccessCode.GET_SITE, response.getData(), response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of sites for the given operating unit ID.
     * </p>
     *
     * @param operatingUnitId {@link Long} The search request contains necessary information
     *                        to get the list of sites is given
     * @return {@link List<Site>} The list of sites for the given operating unit ID is retrieved and retrieved
     */
    @GetMapping("/get-by-ou-id/{operatingUnitId}")
    public List<Site> getSitesByOperatingUnitId(@PathVariable(Constants.OPERATING_UNIT_ID) Long operatingUnitId) {
        return siteService.getSitesByOperatingUnitId(operatingUnitId);
    }

    /**
     * <p>
     * This method is used to get list of site details based on given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The search request contains necessary information
     *                   to get the list of site details is given
     * @return {@link SuccessResponse<Site>} Returns a success message and status with the retrieved
     * list of site details
     */
    @UserTenantValidation
    @PostMapping("/details")
    public SuccessResponse<SiteDetailsDTO> getSiteDetails(@RequestBody CommonRequestDTO requestDto) {
        return new SuccessResponse<>(SuccessCode.GET_SITE, siteService.getSiteDetails(requestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to add a site admin using the provided details.
     * </p>
     *
     * @param user {@link SiteUserDTO} The site user need to be added is given
     * @return {@link SuccessResponse<User>} The user is added for given user details and a success message
     * with the status is returned
     */
    @UserTenantValidation
    @PostMapping("/user-add")
    public SuccessResponse<User> addSiteUser(@RequestBody @Valid SiteUserDTO user) {
        siteService.addSiteAdmin(user);
        return new SuccessResponse<>(SuccessCode.SITE_ADMIN_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to retrieve site by its ID.
     * </p>
     *
     * @param siteId {@link Long} The id of the site that need to be retrieved is given
     * @return {@link ResponseEntity<Site>} The site for the given id is retrieved and returned with the status
     */
    @GetMapping("/get-by-id/{siteId}")
    public ResponseEntity<Site> getSiteById(@PathVariable(Constants.SITE_ID) Long siteId) {
        return ResponseEntity.ok().body(siteService.getSiteById(siteId));
    }

    /**
     * <p>
     * This method is used to update a site admin using the provided details.
     * </p>
     *
     * @param user {@link SiteUserDTO} The site user need to be updated is given
     * @return {@link SuccessResponse<User>} The site user is updated for given user details and a success message
     * with the status is returned
     */
    @UserTenantValidation
    @PutMapping("/user-update")
    public SuccessResponse<User> updateAccountAdmin(@RequestBody @Valid SiteUserDTO user) {
        siteService.updateSiteAdmin(user);
        return new SuccessResponse<>(SuccessCode.SITE_ADMIN_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to delete an site admin for the given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The parameters required to delete the
     *                   site admin is given
     * @return {@link SuccessResponse<User>} The site user for the given request is
     * deleted and success message with status is returned
     */
    @UserTenantValidation
    @DeleteMapping("/user-remove")
    public SuccessResponse<User> deleteAccountAdmin(@RequestBody CommonRequestDTO requestDto) {
        siteService.deleteSiteAdmin(requestDto);
        return new SuccessResponse<>(SuccessCode.SITE_ADMIN_REMOVE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve a list of sites based on the given list of tenant IDs.
     * </p>
     *
     * @param tenants {@link List<Long>} The list of tenantIds that belongs to the list of sites that
     *                need to be retrieved is given
     * @return {@link List<Site>} The list of sites for the given list of tenant IDs is returned
     */
    @PostMapping("/get-sites-by-tenants")
    public List<Site> getSitesByTenantIds(@RequestBody List<Long> tenants) {
        return siteService.getSitesByTenantIds(tenants);
    }

    /**
     * <p>
     * This method is used to retrieve a list of sites based on the given request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The common request contains necessary information
     *                   to get the list of sites is given
     * @return {@link SuccessResponse<List>} The list of sites of the given country and the
     * total count is retrieved and returned with a success message and status
     */
    @PostMapping("/country/site-list")
    public SuccessResponse<List<Site>> getSiteByCountry(@RequestBody CommonRequestDTO requestDto) {
        List<Site> response = siteService.getSiteByCountry(requestDto);
        return new SuccessResponse(SuccessCode.GET_SITE, response, response.size(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get a map of all site IDs and names.
     * </p>
     *
     * @return {@link Map} A map containing site IDs as keys and site names as values is being returned
     */
    @GetMapping("/get-sites")
    public Map<Long, String> getAllSiteIdAndName() {
        return siteService.getAllSiteIdAndName();
    }
}
