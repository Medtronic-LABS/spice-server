package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is a DTO class for account entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class AccountListDTO {
    private long id;
    private String name;
    private Long ouCount;
    private Long siteCount;
    private Long tenantId;

    /**
     * <p>
     * Constructor for AccountListDTO class with id, name, and tenantId parameters.
     * Initializes corresponding instance variables and invokes superclass constructor.
     * </p>
     *
     * @param id       The ID of the account.
     * @param name     The name of the account.
     * @param tenantId The ID of the tenant associated with the account.
     */
    public AccountListDTO(long id, String name, Long tenantId) {
        super();
        this.id = id;
        this.name = name;
        this.tenantId = tenantId;
    }

    /**
     * <p>
     * Constructor for AccountListDTO class
     * </p>
     */
    public AccountListDTO() {
        super();
    }
}
