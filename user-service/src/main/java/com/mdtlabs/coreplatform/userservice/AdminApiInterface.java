package com.mdtlabs.coreplatform.userservice;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * <p>
 * AdminApiInterface is a admin service feign. The methods are used to perform various actions
 * such as clearing API permissions for an account, creating a new country, creating a new account,
 * creating a new operating unit, creating a new site etc.,
 * </p>
 *
 * @author Prabu created on Feb 10, 2023
 */
@FeignClient(name = "admin-service")
public interface AdminApiInterface {

    /**
     * <p>
     * This method is used to clear API permissions for an account using an authorization token.
     * </p>
     *
     * @param token {@link String} It represents the authorization token sent in the request header, and
     *              it is used to authenticate and authorize the user making the request
     *              to clear API permissions for their account
     * @return {@link ResponseEntity<Boolean>} The boolean value is returned with the status indicating
     * whether the API permissions have been successfully cleared or not
     */
    @GetMapping("/account/clear")
    ResponseEntity<Boolean> clearApiPermissions(@RequestHeader("Authorization") String token);

    /**
     * <p>
     * This method is used to create a new country object with the provided data and returns a ResponseEntity
     * containing the created country object.
     * </p>
     *
     * @param token      {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                   authenticate the user making the request
     * @param tenantId   {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                   country is being created
     * @param countryDto {@link CountryDTO} The data required to create a new country is given
     * @return The boolean value is returned with the status indicating whether the creation of country is
     * successful or not
     */
    @PostMapping("/data/country/create")
    ResponseEntity<Country> createCountry(@RequestHeader("Authorization") String token,
                                          @RequestHeader("TenantId") Long tenantId,
                                          @RequestBody CountryDTO countryDto);

    /**
     * <p>
     * This method is used to create a new account with the given account details and authorization and
     * tenant id headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 country is being created
     * @param account  {@link AccountWorkflowDTO} The data required to create a new account is given.
     * @return {@link Account} The created account is returned
     */
    @PostMapping("/account/create")
    Account createAccount(@RequestHeader("Authorization") String token, @RequestHeader("TenantId") Long tenantId,
                          @RequestBody AccountWorkflowDTO account);

    /**
     * <p>
     * This method is used to create a new operating unit with the provided data and authorization token.
     * </p>
     *
     * @param token            {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                         authenticate the user making the request
     * @param tenantId         {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                         country is being created
     * @param operatingunitDto {@link OperatingUnitDTO}  The data required to create a new operating unit is given.
     * @return {@link Operatingunit} The created operating unit is returned
     */
    @PostMapping("/operating-unit/create")
    Operatingunit createOperatingUnit(@RequestHeader("Authorization") String token,
                                      @RequestHeader("TenantId") Long tenantId, @RequestBody OperatingUnitDTO operatingunitDto);

    /**
     * <p>
     * This method is used to create a new site with the given siteDto, using the authorization token and
     * tenant ID provided in the request headers.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request
     * @param tenantId {@link Long} The "TenantId" header is typically used to send the tenant id for which the
     *                 country is being created
     * @param siteDto  {@link SiteDTO}  The data required to create a new site is given.
     * @return {@link Site} The created site is returned
     */
    @PostMapping("/site/create")
    Site createSite(@RequestHeader("Authorization") String token, @RequestHeader("TenantId") Long tenantId,
                    @RequestBody SiteDTO siteDto);

    /**
     * <p>
     * This method is to get the country based on given ID.
     * </p>
     *
     * @param countryId The ID for which the country is being searched is given
     * @return {@link Country} Returns a success message and status with the retrieved country
     */
    @GetMapping("/data/get-country/{id}")
    Country getCountry(@RequestHeader("Authorization") String token,
                       @RequestHeader("TenantId") Long tenantId, @PathVariable(value = Constants.ID) long countryId);

    /**
     * <p>
     * This method is to get the county based on given ID.
     * </p>
     *
     * @param id The ID for which the county is being searched is given
     * @return {@link County} Returns a success message with the retrieved county
     */
    @GetMapping("/data/county/get/{id}")
    County getCountyById(@RequestHeader("Authorization") String token,
                         @RequestHeader("TenantId") Long tenantId, @PathVariable(value = Constants.ID) long id);

    /**
     * <p>
     * This method is to get the sub county based on given ID.
     * </p>
     *
     * @param subCountyId The ID for which the sub county is being searched is given
     * @return {@link Subcounty} Returns a success message and status with the retrieved sub county
     */
    @GetMapping("/data/subcounty/{id}")
    Subcounty getSubCountyById(@RequestHeader("Authorization") String token,
                                      @RequestHeader("TenantId") Long tenantId,
                                      @PathVariable(value = Constants.ID) long subCountyId);
}
