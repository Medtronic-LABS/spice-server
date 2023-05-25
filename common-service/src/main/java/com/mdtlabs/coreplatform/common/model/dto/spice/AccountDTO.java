package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * This is an DTO class for Account entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AccountDTO {
    private Long id;

    private String name;

    private int maxNoOfUsers;

    private Date updatedAt;

    private Long tenantId;

    private CountryDTO country;

    private String status;
}
