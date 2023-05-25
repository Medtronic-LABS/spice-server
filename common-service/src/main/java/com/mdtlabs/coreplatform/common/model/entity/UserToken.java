package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * UserToken is a Java class representing a user token entity with fields for user ID, authentication token, and client
 * information.
 * </p>
 *
 * @author Prabu created on 17 Feb 2023
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_USER_TOKEN)
public class UserToken extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.USER_ID)
    private Long userId;

    @Column(name = FieldConstants.AUTH_TOKEN)
    private String authToken;

    @Column(name = FieldConstants.CLIENT)
    private String client;
}