package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * This class is an Request DTO class for device details.
 * </p>
 *
 * @author Prabu created on 16 Feb 2023
 */
@Data
public class DeviceDetailsDTO {

    private Long id;

    private String name;

    private String type;

    private String model;

    private String version;

    private String aesKey;

    private String deviceId;

    private Long userId;

    private Long tenantId;

    private String rsaPublicKey;

    private String rsaPrivateKey;

    private String authTag;

    private Date lastLoggedIn;

    private String refId;
}
