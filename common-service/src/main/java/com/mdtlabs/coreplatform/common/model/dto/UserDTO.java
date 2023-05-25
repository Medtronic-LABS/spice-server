package com.mdtlabs.coreplatform.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
public class UserDTO {

    private Long id;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private boolean isActive;

    private boolean isDeleted;

    private String username;

    private String password;

    private String gender;

    private Set<Role> roles = new HashSet<>();

    private boolean active;

    private Date blockedDate;

    private String forgetPasswordToken;

    private Date forgetPasswordTime;

    private int forgetPasswordCount;

    private Date invalidLoginTime;

    private int invalidLoginAttempts;

    private Date invalidResetTime;

    private Boolean isPasswordResetEnabled;

    private int passwordResetAttempts;

    private boolean isLicenseAcceptance;

    private Date lastLoggedIn;

    private Date lastLoggedOut;

    private String address;

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

    private Country country;

    private Timezone timezone;

    private Set<Organization> organizations = new HashSet<>();

    private Boolean isBlocked;

    private String countryCode;

    private Long deviceInfoId;

    private long tenantId;

    private Boolean isSuperUser = false;

    private Long cultureId;

    /**
     * This function returns a TimezoneDTO object mapped from a timezone object using ModelMapper.
     *
     * @return A {@link TimezoneDTO} object is being returned. If the "timezone" object is null, then null is
     * returned. The method uses a ModelMapper to map the "timezone" object to a TimezoneDTO object.
     */
    public TimezoneDTO getTimezone() {
        ModelMapper modelMapper = new ModelMapper();
        if (Objects.isNull(timezone)) {
            return null;
        }
        return modelMapper.map(timezone, TimezoneDTO.class);
    }

    /**
     * This function returns a set of RoleDTO objects mapped from a list of roles using ModelMapper.
     *
     * @return {@link Set<RoleDTO>} is being returned. If the roles object is null, an empty set is
     * returned. Otherwise, the roles are mapped to RoleDTO objects using ModelMapper and then
     * collected into a set using Collectors.toSet().
     */
    public Set<RoleDTO> getRoles() {
        if (Objects.isNull(roles)) {
            return Collections.emptySet();
        }
        return roles.stream().map(role -> new ModelMapper().map(role, RoleDTO.class)).collect(Collectors.toSet());
    }

    /**
     * This Java function returns a CountryDTO object mapped from a country object using ModelMapper.
     *
     * @return A {@link CountryDTO} object is being returned.
     */
    public CountryDTO getCountry() {
        ModelMapper modelMapper = new ModelMapper();
        if (Objects.isNull(country)) {
            return null;
        }
        return modelMapper.map(country, CountryDTO.class);
    }

    /**
     * This method is used to get user value
     *
     * @return String - user value
     */
    @JsonIgnore
    public long getUserValue() {
        if (null != UserContextHolder.getUserDto()) {
            return UserContextHolder.getUserDto().getId();
        }
        return Constants.ZERO;
    }

}
