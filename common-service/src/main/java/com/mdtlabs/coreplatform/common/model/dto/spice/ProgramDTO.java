package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.Country;
import lombok.Data;

/**
 * <p>
 * The ProgramDTO class contains data fields for a program's ID, deletion status, activity status,
 * name, associated country, account ID, operating unit ID, and site ID.
 * </p>
 */
@Data
public class ProgramDTO {

    private long id;

    private Boolean isDeleted;

    private Boolean isActive;

    private String name;

    private Country country;

    private long accountId;

    private long operatingUnitId;

    private long siteId;

}
