package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * This class is an DTO used to represent users on a Site.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Data
public class SiteUserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String phoneNumber;

    private String gender;

    private Country country;

    private Timezone timezone;

    private String countryCode;

    private Set<Role> roles = new HashSet<>();

    private boolean isRedRisk;

    private Long tenantId;

    private Long cultureId;
}
