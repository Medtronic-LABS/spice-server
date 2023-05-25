package com.mdtlabs.coreplatform.spiceservice;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OtherMedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Program;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * AdminApiInterface is a admin service feign. The methods are used to perform various actions
 * such as  retrieving information related to medication, lab tests, countries, counties, subcounties, sites,
 * accounts, workflows, customizations, programs, and lab test results.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@FeignClient(name = "admin-service")
public interface AdminApiInterface {

    /**
     * <p>
     * This method is used to retrieve information about other medications for a specific country.
     * </p>
     *
     * @param token     {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                  authenticate the user making the request is given
     * @param tenantId  {@link Long} The "TenantId" header represents the unique identifier of the tenant or
     *                  organization that the request is being made for is given
     * @param countryId The `countryId` represents the unique identifier of a country is given
     * @return {@link ResponseEntity<OtherMedicationDTO>} The ResponseEntity contains OtherMedication is returned with
     * status
     */
    @GetMapping("/medication/other-medication/{countryId}")
    ResponseEntity<OtherMedicationDTO> getOtherMedication(@RequestHeader("Authorization") String token,
                                                          @RequestHeader("TenantId") Long tenantId, @PathVariable long countryId);

    /**
     * <p>
     * This method is used to retrieve a lab test by name using authorization token, tenant ID, and search request
     * DTO.
     * </p>
     *
     * @param token            {@link String} The "Authorization" header is typically used to send a token or
     *                         credentials to authenticate the user making the request is given
     * @param tenantId         {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                         lab test is being requested is given
     * @param searchRequestDto {@link SearchRequestDTO} The searchRequestDto contains the search criteria for retrieving
     *                         a LabTest by name is given
     * @return {@link ResponseEntity<LabTest>} The ResponseEntity contains a LabTest is returned with status
     */
    @PostMapping("/labtest/patient-labtest/get-by-name")
    ResponseEntity<LabTest> getLabTestByName(@RequestHeader("Authorization") String token,
                                             @RequestHeader("TenantId") Long tenantId, @RequestBody SearchRequestDTO searchRequestDto);

    /**
     * <p>
     * This method is used to retrieve a list of LabTest objects by their IDs, with authorization and tenant ID headers.
     * </p>
     *
     * @param token      {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                   authenticate the user making the request is given
     * @param tenantId   {@link Long} The "TenantId" header is typically used to send the tenant id for  whom the
     *                   lab tests are being requested is given
     * @param labTestIds {@link Set<Long>} The set of Long values representing the IDs of the lab tests that need to
     *                   be retrieved is given
     * @return {@link ResponseEntity<List<LabTest>>} The ResponseEntity containing a List of LabTest is being returned
     * with status
     */
    @PostMapping("/labtest/patient-labtest/get-list-by-ids")
    ResponseEntity<List<LabTest>> getLabTestsByIds(@RequestHeader("Authorization") String token,
                                                   @RequestHeader("TenantId") Long tenantId, @RequestBody Set<Long> labTestIds);

    /**
     * <p>
     * This method is used to retrieves a country by its ID, using authorization and tenant ID headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header represents the ID of the tenant for which the
     *                 country data is being requested is given
     * @param id       The "id" represents the unique identifier of the country that needs to be retrieved is given
     * @return {@link Country} The Country is being returned
     */
    @GetMapping("/data/get-country/{id}")
    Country getCountryById(@RequestHeader("Authorization") String token,
                           @RequestHeader("TenantId") Long tenantId, @PathVariable(value = "id") long id);

    /**
     * <p>
     * This method is used to retrieves a list of counties based on a given country ID, with authorization and tenant ID
     * headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is used to identify the specific tenant and retrieve
     *                 data related to that tenant from the database is given
     * @param id       The "id" represents the "id" of the country for which the list of counties is being requested
     *                 is given
     * @return {@link List<County>} The list of County objects is being returned
     */
    @GetMapping(value = "/data/county-list/{id}")
    List<County> getAllCountyByCountryId(@RequestHeader("Authorization") String token,
                                         @RequestHeader("TenantId") Long tenantId, @PathVariable(value = "id") long id);

    /**
     * <p>
     * This method is used to retrieve a list of subcounties based on a given country ID.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 subcounties are being retrieved is given
     * @param id       The "id" represents the unique identifier of a country. It is used to
     *                 retrieve a list of all sub-counties that belong to the specified country is given
     * @return {@link List<Subcounty>} The list of Subcounty is being returned.
     */
    @GetMapping(value = "/data/subcounty-list/{id}")
    List<Subcounty> getAllSubCountyByCountryId(@RequestHeader("Authorization") String token,
                                               @RequestHeader("TenantId") Long tenantId, @PathVariable(value = "id") long id);

    /**
     * <p>
     * This method is used to retrieve a list of sites based on a list of tenant IDs and a specific tenant ID provided
     * in the request headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header represents the ID of a specific tenant. It is
     *                 used as a filter to retrieve a list of sites that belong to the specified tenant is given
     * @param tenants  {@link List<Long>} The list of Long values representing the IDs of tenants for which the sites
     *                 need to be retrieved is given
     * @return {@link List<Site>} The list of Site is being returned
     */
    @PostMapping(value = "/site/get-sites-by-tenants")
    List<Site> getSitesByTenantIds(@RequestHeader("Authorization") String token,
                                   @RequestHeader("TenantId") Long tenantId, @RequestBody List<Long> tenants);

    /**
     * <p>
     * This method is used to retrieve an account by its ID, using authorization and tenant ID headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 account is being retrieved is given
     * @param id       The "id" is used to extract the value of the "id" variable from
     *                 the URL path. In this case, it represents the account that the user wants to retrieve  is given
     * @return {@link Account} The account for the given details is returned
     */
    @GetMapping("/account/get-account/{id}")
    Account getAccountById(@RequestHeader("Authorization") String token,
                           @RequestHeader("TenantId") Long tenantId, @PathVariable(value = "id") long id);

    /**
     * <p>
     * This method is used to retrieve a list of all account workflows for a given tenant ID and authorization token.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 account workflows are being retrieved is given
     * @return {@link List<AccountWorkflow>} The list of AccountWorkflow is being returned.
     */
    @PostMapping("/clinical-workflow/get-all-workflows")
    List<AccountWorkflow> getAllAccountWorkFlows(@RequestHeader("Authorization") String token,
                                                 @RequestHeader("TenantId") Long tenantId);

    /**
     * <p>
     * This method is a POST request that retrieves a list of account customization data based on the provided
     * authorization token, tenant ID, and request body.
     * </p>
     *
     * @param token       {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                    authenticate the user making the request is given
     * @param tenantId    {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                    account Customization are being retrieved is given
     * @param requestData {@link Map} The requestData is a Map object that contains the request data sent in the request
     *                    body is given
     * @return {@link List<AccountCustomization>} The List of AccountCustomization is being returned.
     */
    @PostMapping("/account-customization/static-data/get-list")
    List<AccountCustomization> getAccountCustomization(@RequestHeader("Authorization") String token,
                                                       @RequestHeader("TenantId") Long tenantId, @RequestBody Map<String, Object> requestData);

    /**
     * <p>
     * This method is used to retrieve a list of region customizations based on the provided culture ID, tenant ID, and
     * authorization token.
     * </p>
     *
     * @param token     {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                  authenticate the user making the request is given
     * @param tenantId  The `tenantId` for which the region customizations are being retrieved is given
     * @param cultureId {@link Long} The cultureId represents the ID of the culture for which the region
     *                  customizations are being requested is given
     * @return {@link List<RegionCustomization>} The list of RegionCustomization is being returned.
     */
    @PostMapping("/region-customization/static-data/get-list/{cultureId}")
    List<RegionCustomization> getRegionCustomizations(@RequestHeader("Authorization") String token,
                                                      @RequestHeader("TenantId") long tenantId, @PathVariable(Constants.CULTURE_ID) Long cultureId);

    /**
     * <p>
     * This method is used to retrieve a list of sites based on the provided operating unit ID, along with authorization
     * and tenant ID headers.
     * </p>
     *
     * @param token           {@link String} The "Authorization" header is typically used to send a token or
     *                        credentials to authenticate the user making the request is given
     * @param tenantId        {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                        list of site search is being performed is given
     * @param operatingUnitId {@link Long} The operating unit for which the list of sites needs to
     *                        be retrieved is given
     * @return {@link List<Site>} The list of Site that belong to the Operating Unit with the specified ID
     * is returned
     */
    @GetMapping("/site/get-by-ou-id/{operatingUnitId}")
    List<Site> getSitesByOperatingUnitId(@RequestHeader("Authorization") String token,
                                         @RequestHeader("TenantId") Long tenantId, @PathVariable(value = "operatingUnitId") Long operatingUnitId);

    /**
     * <p>
     * This method is used to retrieve a list of programs based on a list of site IDs and authorization and tenant
     * ID headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 list of program search is being performed is given
     * @param siteIds  {@link List<Long>} The siteIds is a list of Long values representing the unique identifiers
     *                 of sites for which the corresponding programs are to be retrieved is given
     * @return {@link List<Program>} The list of Program is being returned
     */
    @PostMapping("/program/get-by-site-ids")
    List<Program> getPrograms(@RequestHeader("Authorization") String token,
                              @RequestHeader("TenantId") Long tenantId, @RequestBody List<Long> siteIds);

    /**
     * <p>
     * This method is used to retrieve a site by its ID, using authorization and tenant ID headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 site search is being performed is given
     * @param siteId   {@link Long} The siteId is used in the URL path to specify which site to retrieve is given
     * @return {@link ResponseEntity<Site>} The site for the given details is returned with status
     */
    @GetMapping(value = "/site/get-by-id/{siteId}")
    ResponseEntity<Site> getSiteById(@RequestHeader("Authorization") String token,
                                     @RequestHeader("TenantId") Long tenantId, @PathVariable("siteId") Long siteId);

    /**
     * <p>
     * This method is used to receive a POST request with authorization and tenant headers, and a request body, and
     * returns a list of LabTest.
     * </P>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 lab test search is being performed is given
     * @param request  {@link RequestDTO} The RequestDTO is used to pass additional parameters or filters for
     *                 searching lab tests is given
     * @return {@link List<LabTest>} The list of LabTest is being returned
     */
    @PostMapping(value = "/labtest/search")
    List<LabTest> getLabtest(@RequestHeader("Authorization") String token,
                             @RequestHeader("TenantId") Long tenantId, @RequestBody RequestDTO request);

    /**
     * <p>
     * This method is used to retrieve a list of lab test results for a specific lab test ID
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 lab test results are being requested is given
     * @param id       {@link Long} The id is used to identify a specific lab test result is given
     * @return {@link List<LabTestResultDTO>} The list of LabTestResult is being returned
     */
    @GetMapping(value = "/labtest/labtest-result/{id}")
    List<LabTestResultDTO> getLabtestResults(@RequestHeader("Authorization") String token,
                                             @RequestHeader("TenantId") Long tenantId, @PathVariable(value = "id") Long id);

    /**
     * <p>
     * This method is used to return a map of all site IDs and names.
     * </p>
     *
     * @return {@link Map} The map containing site IDs as keys and site names as values is being returned
     */
    @GetMapping("/site/get-sites")
    Map<Long, String> getAllSiteIdAndName();
}
