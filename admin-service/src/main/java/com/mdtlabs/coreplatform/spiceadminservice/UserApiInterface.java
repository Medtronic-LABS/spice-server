package com.mdtlabs.coreplatform.spiceadminservice;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.User;

/**
 * <p>
 * UserApiInterface is a interface that is used to communicate with user service.
 * </p>
 *
 * @author Niraimathi S
 */
@FeignClient(name = "user-service")
public interface UserApiInterface {

    /**
     * <p>
     * This method is used to retrieve a list of user organization DTOs based on a list of tenant IDs and the
     * user's own tenant ID.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header in the HTTP request, which is typically used
     *                     to send a token or credentials for authentication purposes is given
     * @param userTenantId {@link Long} The ID of the tenant to which the user associated is given
     * @param tenantIds    {@link List<Long>} The IDs of the tenants for which the users need to be retrieved is given
     * @return {@link List<UserOrganizationDTO>} A list of user organization DTOs for given details is retrieved
     * and returned.
     */
    @PostMapping("/user/get-by-tenants")
    List<UserOrganizationDTO> getUsersByTenantIds(@RequestHeader("Authorization") String token,
                                                  @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @RequestBody List<Long> tenantIds);

    /**
     * <p>
     * This method is used to add an admin user to an organization with the option to mark them as a red risk.
     * </p>
     *
     * @param token     {@link String} The "Authorization" header in the HTTP request, which is typically
     *                  used to send a token or credentials for authentication purposes is given
     * @param tenantId  {@link Long} The ID of the tenant to which the user associated is given
     * @param userDto   {@link UserDetailsDTO} The details of a user that needs to be added as an admin user for
     *                  an organization is given
     * @param isRedRisk A boolean value to indicate whether the user being added is a red risk user or not is given
     * @return {@link ResponseEntity<User>} The user is created for the given details and returned with status
     */
    @PostMapping("/organization/add-admin-user")
    ResponseEntity<User> addAdminUser(@RequestHeader("Authorization") String token,
                                      @RequestHeader(Constants.HEADER_TENANT_ID) Long tenantId, @RequestBody UserDetailsDTO userDto,
                                      @RequestParam boolean isRedRisk);

    /**
     * <p>
     * This method is used to update an organization with the provided organization DTO, using the authorization
     * token and user tenant ID from the request headers.
     * </p>
     *
     * @param token           {@link String} The "Authorization" header in the HTTP request, which is typically used to
     *                        send a token or credentials for authentication purposes is given
     * @param userTenantId    {@link Long} The ID of the tenant to which the user associated is given
     * @param organizationDto {@link OrganizationDTO} The updated information for an organization is given
     * @return {@link Organization} The organization is updated for the given details and returned
     */
    @PutMapping("/organization/update")
    Organization updateOrganization(@RequestHeader("Authorization") String token,
                                    @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @RequestBody OrganizationDTO organizationDto);

    /**
     * <p>
     * This method is used to update an admin user in an organization with the provided user DTO and authorization token.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header in the HTTP request, which is typically used to send
     *                     a token or credentials for authentication purposes is given
     * @param userTenantId {@link Long} The ID of the tenant to which the user associated is given
     * @param userDto      {@link UserDTO} The updated information for an admin user is given
     * @return {@link ResponseEntity<User>} The user is updated for the given details and returned with status
     */
    @PutMapping("/organization/update-admin-user")
    ResponseEntity<User> updateAdminUser(@RequestHeader("Authorization") String token,
                                         @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @RequestBody UserDTO userDto);

    /**
     * <p>
     * This method is used to  delete an admin user from an organization using authorization token and tenant ID.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header in the HTTP request, which is typically used to send a
     *                     token or credentials for authentication purposes is given
     * @param userTenantId {@link Long} The ID of the tenant to which the user associated is given
     * @param requestDto   {@link CommonRequestDTO} The search request contains necessary information to delete the
     *                     admin user is given
     * @return {@link ResponseEntity<Organization>} A boolean value indicating whether the deletion of an admin user was
     * successful or not is returned with status
     */
    @DeleteMapping("/organization/delete-admin-user")
    ResponseEntity<Boolean> deleteAdminUser(@RequestHeader("Authorization") String token,
                                            @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @RequestBody CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to retrieve details of an organization by its ID, using authorization and tenant ID headers.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header in the HTTP request, which is typically used
     *                     to send a token or credentials for authentication purposes is given
     * @param userTenantId {@link Long} The ID of the tenant to which the user associated is given
     * @param id           {@link Long} The ID of the organization which need to searched is given
     * @return {@link ResponseEntity<Organization>} The organization for the given id is returned with status
     */
    @GetMapping("/organization/details/{id}")
    ResponseEntity<Organization> getOrganizationById(@RequestHeader("Authorization") String token,
                                                     @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @PathVariable("id") Long id);

    /**
     * <p>
     * This method is used to activate or deactivate a list of organizations based on a boolean value.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header in the HTTP request, which is typically used
     *                     to send a token or credentials for authentication purposes is given
     * @param userTenantId {@link Long} The ID of the tenant to which the user associated is given
     * @param tenantIds    {@link List<Long>} The IDs of the tenants for which the organization's active status
     *                     needs to be updated is given
     * @param isActive     {@link Boolean} A boolean value that is used to specify whether the organization
     *                     should be activated or deactivated is given
     * @return {@link Boolean} A boolean value is indicating the active status of the organization is returned.
     */
    @PostMapping("/organization/activate-deactivate")
    Boolean activateOrDeactivateOrg(@RequestHeader("Authorization") String token,
                                    @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @RequestBody List<Long> tenantIds,
                                    @RequestParam Boolean isActive);

    /**
     * <p>
     * This method is used to update the active status of a user for a list of tenant IDs.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header in the HTTP request, which is typically used to
     *                     send a token or credentials for authentication purposes is given
     * @param userTenantId {@link Long} The ID of the tenant to which the user associated is given
     * @param tenantIds    {@link List<Long>} The IDs of the tenants for which the user's active status
     *                     needs to be updated is given
     * @param isActive     A boolean value that is used to specify whether the user should be activated or
     *                     deactivated is given
     */
    @PostMapping("/user/update-active-status")
    void activateOrDeactivateUser(@RequestHeader("Authorization") String token,
                                  @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @RequestBody List<Long> tenantIds,
                                  @RequestParam("isActive") boolean isActive);

    /**
     * <p>
     * This method is used to update a site user for an organization with the provided authorization token, user
     * tenant ID, and user data.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header in the HTTP request, which is typically
     *                     used to send a token or credentials for authentication purposes is given
     * @param userTenantId {@link Long} The ID of the tenant to which the user associated is given
     * @param user         {@link SiteUserDTO} The updated information for a site user is given
     * @return {@link ResponseEntity<User>} The updated user for the given details is returned with status
     */
    @PutMapping("/organization/update-site-user")
    ResponseEntity<User> updateSiteUser(@RequestHeader("Authorization") String token,
                                        @RequestHeader(Constants.HEADER_TENANT_ID) Long userTenantId, @RequestBody SiteUserDTO user);
}
