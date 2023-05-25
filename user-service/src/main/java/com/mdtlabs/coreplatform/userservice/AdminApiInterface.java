package com.mdtlabs.coreplatform.userservice;

import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
