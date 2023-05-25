package com.mdtlabs.coreplatform.userservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * <p>
 * This interface is for feign that communicates with a spice service.
 * </p>
 *
 * @author Prabu created on Feb 10, 2023
 */
@FeignClient(name = "spice-service")
public interface SpiceApiInterface {

    /**
     * <p>
     * This method is used to clear API permissions for a user with a given authorization token.
     * </p>
     *
     * @param token {@link String} It represents the authorization token sent in the request header, and it
     *              is used to authenticate and authorize the user making the request to
     *              access the "clearApiPermissions" endpoint
     * @return {@link ResponseEntity<Boolean>} The boolean value is returned with the status whether the
     * map is cleared or not
     */
    @GetMapping("/assessment/clear")
    ResponseEntity<Boolean> clearApiPermissions(@RequestHeader("Authorization") String token);
}
