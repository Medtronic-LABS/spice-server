package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.RoleDTO;
import com.mdtlabs.coreplatform.common.model.dto.TimezoneDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * This is a DTO class for user entity.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@Data
@JsonInclude(Include.NON_NULL)
public class UserListDTO {

    private long id;

    private String username;

    private String gender;

    private Set<Role> roles = new HashSet<>();

    private boolean active;

    private String firstName;

    private String lastName;

    private String middleName;

    private String phoneNumber;

    private Country country;

    private Timezone timezone;

    private Set<Organization> organizations;

    private Boolean isBlocked;

    private String countryCode;

    private Long deviceInfoId;

    private long tenantId;

    private Boolean isSuperUser = false;

    private String defaultOrganizationName;

    private String defaultRoleName;

    private boolean isRedRisk;

    private Long cultureId;


    /**
     * <p>
     * This function maps a timezone object to a timezone DTO using a ModelMapper.
     * </p>
     *
     * @return {@link TimezoneDTO} object is being returned is given
     */
    public TimezoneDTO getTimezone() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(timezone, TimezoneDTO.class);
    }

    /**
     * <p>
     * This function returns a set of RoleDTO objects by mapping each element of the roles list to a
     * RoleDTO object using ModelMapper.
     * </p>
     *
     * @return {@link Set<RoleDTO>} objects is being returned is given
     */
    public Set<RoleDTO> getRoles() {
        return roles.stream().map(role -> new ModelMapper().map(role, RoleDTO.class)).collect(Collectors.toSet());
    }

    /**
     * <p>
     * This Java function returns a CountryDTO object mapped from a country object using ModelMapper.
     * </p>
     *
     * @return {@link CountryDTO} object is being returned is given
     */
    public CountryDTO getCountry() {
        ModelMapper modelMapper = new ModelMapper();
        if (Objects.isNull(country)) {
            return null;
        }
        return modelMapper.map(country, CountryDTO.class);
    }

    /**
     * <p>
     * This function returns the name of the default organization based on the tenant ID.
     * </p>
     *
     * @return The method is returning a String value which is the name of the default organization.
     */
    public String getDefaultOrganizationName() {
        Organization organization = organizations.stream().filter(org -> Objects.equals(org.getId(), this.tenantId))
                .findFirst().orElse(null);
        defaultOrganizationName = !Objects.isNull(organization) ? organization.getName() : null;
        return defaultOrganizationName;
    }

    /**
     * <p>
     * This function returns the default role name based on a list of roles and predefined constants.
     * </p>
     *
     * @return The method is returning a String value, which is the default role name.
     */
    public String getDefaultRoleName() {
        List<String> roleNames = roles.stream().map(Role::getName).toList();
        defaultRoleName = roleNames.stream().filter(Constants.SPICE_WEB_ROLES.values()::contains).findFirst().orElse(null);
        defaultRoleName = Objects.isNull(defaultRoleName)
                ? roleNames.stream().filter(Constants.SPICE_MOBILE_ROLES.values()::contains).findFirst().orElse(null)
                : defaultRoleName;
        defaultRoleName = Objects.isNull(defaultRoleName)
                ? roleNames.stream().filter(Constants.SPICE_EMR_ROLES.values()::contains).findFirst().orElse(null)
                : defaultRoleName;
        return defaultRoleName;
    }

    /**
     * <p>
     * This Java function checks if a user has the role of a red risk user and returns a boolean value.
     * </p>
     *
     * @return The method `isRedRisk()` returns a boolean value indicating whether the `roles` list
     * contains a role with the name `Constants.ROLE_RED_RISK_USER`.
     */
    public boolean isRedRisk() {
        isRedRisk = roles.stream().anyMatch(role -> role.getName().equals(Constants.ROLE_RED_RISK_USER));
        return isRedRisk;
    }
}
