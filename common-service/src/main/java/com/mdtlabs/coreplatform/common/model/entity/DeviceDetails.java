package com.mdtlabs.coreplatform.common.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * The class DeviceDetails represents a device with various properties such as name, type, model, version,
 * keys, and user information.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_DEVICE_DETAILS)
public class DeviceDetails extends BaseEntity {

    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.MODEL)
    private String model;

    @Column(name = FieldConstants.VERSION)
    private String version;

    @Column(name = FieldConstants.AES_KEY)
    private String aesKey;

    @Column(name = FieldConstants.DEVICE_ID)
    private String deviceId;

    @Column(name = FieldConstants.USER_ID)
    private Long userId;

    @Column(name = FieldConstants.TENANT_ID)
    private Long tenantId;

    @Column(name = FieldConstants.RSA_PUBLIC_KEY)
    private String rsaPublicKey;

    @Column(name = FieldConstants.RSA_PRIVATE_KEY)
    private String rsaPrivateKey;

    @Column(name = FieldConstants.AUTH_TAG)
    private String authTag;

    @Column(name = FieldConstants.LAST_LOGGED_IN)
    private Date lastLoggedIn;
}