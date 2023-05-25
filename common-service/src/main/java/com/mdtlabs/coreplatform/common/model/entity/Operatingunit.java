package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Operatingunit is a Java class representing an operating unit entity
 * with fields for name, account, country ID, and tenant ID.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_OPERATING_UNIT)
@DynamicUpdate
public class Operatingunit extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -2584864712605974827L;

    @NotBlank
    @Column(name = FieldConstants.NAME, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = FieldConstants.ACCOUNT_ID)
    private Account account;

    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @Column(name = FieldConstants.TENANT_ID)
    private Long tenantId;
}
