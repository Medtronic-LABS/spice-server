package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * The PrescriberDTO class contains data fields for a prescriber's first name, last name, country code,
 * phone number, last refill date, and last refill visit ID.
 * </p>
 */
@Data
public class PrescriberDTO {

    private String firstName;

    private String lastName;

    private String countryCode;

    private String phoneNumber;

    private Date lastRefillDate;

    private long lastRefillVisitId;

}
