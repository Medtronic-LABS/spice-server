package com.mdtlabs.coreplatform.common.model.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import org.modelmapper.ModelMapper;

import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;

/**
 * <p>
 * This is a DTO class for user entity.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@Data
public class AuthUserDTO {

    private long id;

    private String username;

    private String gender;

    private Set<Role> roles = new HashSet<>();

    private boolean active;

    private boolean accountExpired;

    private boolean accountLocked;

    private boolean credentialsExpired;

    private String authorization;

    private long currentDate;

    private String firstName;

    private String lastName;

    private String middleName;

    private String subject;

    private String phoneNumber;

    private Timezone timezone;

    private Set<Organization> organizations;

    private long tenantId;

    private String countryCode;

    private Boolean isBlocked;

    private Country country;

    private Long deviceInfoId;

    private Boolean isSuperUser = false;

    private Long cultureId;

    /**
     * <p>
     * This function returns a set of RoleDTO objects by mapping each element of the roles list to a
     * RoleDTO object using ModelMapper.
     * </p>
     *
     * @return A set of RoleDTO objects is being returned. The method uses Java 8 stream and map
     * functions to convert each Role object in the roles list to a RoleDTO object using ModelMapper,
     * and then collects them into a new set using Collectors.toSet().
     */
    public Set<RoleDTO> getRoles() {
        return roles.stream().map(role -> new ModelMapper().map(role, RoleDTO.class)).collect(Collectors.toSet());
    }

    /**
     * <p>
     * This function maps a timezone object to a TimezoneDTO object using ModelMapper.
     * </p>
     *
     * @return {@link TimezoneDTO} A TimezoneDTO object is being returned
     */
    public TimezoneDTO getTimezone() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(timezone, TimezoneDTO.class);
    }

    /**
     * <p>
     * This function returns a CountryDTO object mapped from a country object using ModelMapper.
     * </p>
     *
     * @return {@link CountryDTO} A CountryDTO object is being returned
     */
    public CountryDTO getCountry() {
        if (Objects.isNull(country)) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(country, CountryDTO.class);
    }

    /**
     * <p>
     * This function returns a set of AuthOrganizationDTO objects by mapping the organizations list to AuthOrganizationDTO
     * using ModelMapper.
     * </p>
     *
     * @return {@link Set} A set of AuthOrganizationDTO objects is being returned
     */
    public Set<AuthOrganizationDTO> getOrganizations() {
        return this.organizations.stream().map(org -> new ModelMapper().map(org, AuthOrganizationDTO.class)).collect(Collectors.toSet());
    }
}
