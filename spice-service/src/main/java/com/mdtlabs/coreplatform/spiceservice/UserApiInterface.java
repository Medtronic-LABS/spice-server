package com.mdtlabs.coreplatform.spiceservice;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.Organization;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * UserApiInterface interface is used to access user service APIs using Feign client.
 * </p>
 *
 * @author Niraimathi S
 */
@FeignClient(name = "user-service")
public interface UserApiInterface {

    /**
     * <p>
     * This method is used to retrieve the details of a user with a specific ID, using authentication and
     * tenant ID headers.
     * </p>
     *
     * @param authToken {@link String} The authToken is used to authenticate the user and ensure that they have the
     *                  necessary permissions to access the requested resource is given
     * @param tenantId  {@link Long} The `tenantId` represents the ID of the tenant for which the user details are
     *                  being requested is given
     * @param id        The id represents the unique identifier of the user whose details are being requested is given
     * @return {@link UserListDTO} The UserListDTO for the given details is returned
     */
    @GetMapping("/user/details/{id}")
    UserListDTO getPrescriberDetails(@RequestHeader("Authorization") String authToken,
                                     @RequestHeader(Constants.HEADER_TENANT_ID) Long tenantId, @PathVariable long id);

    /**
     * <p>
     * This method is used to retrieve a map of UserDTO objects by their IDs, with authorization and tenant
     * ID headers.
     * </p>
     *
     * @param authToken {@link String} The authToken is used to authenticate the user and ensure that they have the
     *                  necessary permissions to access the requested resource is given
     * @param tenantId  {@link Long} The `tenantId` represents the ID of the tenant for which the users are being
     *                  requested is given
     * @param ids       {@link Set<Long>} The "ids" represents the unique identifiers of the users that the
     *                  client wants to retrieve is given
     * @return {@link ResponseEntity<Map<Long, UserDTO>>} The ResponseEntity object contains a Map of UserDTO objects
     * with their corresponding Long ids is returned with status
     */
    @PostMapping("/user/get-by-ids")
    ResponseEntity<Map<Long, UserDTO>> getUsers(@RequestHeader("Authorization") String authToken,
                                                @RequestHeader(Constants.HEADER_TENANT_ID) Long tenantId, @RequestBody Set<Long> ids);

    /**
     * <p>
     * This method is used to retrieve a list of users by their role name, with authorization and tenant ID headers.
     * </p>
     *
     * @param authToken {@link String} The authToken is used to authenticate the user and ensure that they have the
     *                  necessary permissions to access the requested resource is given
     * @param tenantId  {@link Long} The `tenantId` represents the ID of the tenant for which the users are being
     *                  requested is given
     * @param roleName  {@link String} The roleName represents the name of the role for which you want to
     *                  retrieve the list of users is given
     * @return {@link ResponseEntity<List<UserResponseDTO>>} The list of user response for the given details is
     * returned with status
     */
    @PostMapping("/user/get-by-rolename")
    ResponseEntity<List<UserResponseDTO>> getUsersByRoleName(@RequestHeader("Authorization") String authToken,
                                                             @RequestHeader(Constants.HEADER_TENANT_ID) Long tenantId, @RequestBody String roleName);

    /**
     * <p>
     * This  method is used to retrieve organization details by form data ID and form name.
     * </p>
     *
     * @param token          {@link String} The "Authorization" contains the authentication token for the user making
     *                       the request is given
     * @param tenantId       The `tenantId` represents the ID of the tenant for which the users are being
     *                       requested is given
     * @param organizationId The organization ID represents the unique identifier of the
     *                       organization's form data is given
     * @param formName       {@link String} The "formName" represents the name of the form is given
     * @return {@link ResponseEntity<Organization>} The Organization for the given details is returned with status
     */
    @GetMapping(value = "/organization/details/{formDataId}/{formName}")
    ResponseEntity<Organization> getOrganizationByFormDataIdAndName(@RequestHeader("Authorization") String token,
                                                                    @RequestHeader(Constants.HEADER_TENANT_ID) long tenantId,
                                                                    @PathVariable(Constants.FORM_DATA_ID) long organizationId, @PathVariable(Constants.FORM_NAME) String formName);
}
