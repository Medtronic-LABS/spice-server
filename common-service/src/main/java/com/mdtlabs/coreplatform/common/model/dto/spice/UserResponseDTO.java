package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is a DTO class for user entity.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@Data
public class UserResponseDTO {

    private long id;

    private String username;

    private String phoneNumber;

    private String countryCode;

    private long tenantId;

}
