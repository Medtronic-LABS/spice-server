package com.mdtlabs.coreplatform.common.model.dto;

import lombok.Data;

/**
 * <p>
 * This class is an entity class for timezone table.
 * </p>
 *
 * @author Prabu created on Dec 06 2022
 */
@Data
public class TimezoneDTO {

    private long id;

    private String offset;

    private String description;

}
