package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.dto.TimezoneDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * This is a Java class representing user details with various fields and methods for mapping timezone
 * and country data to DTOs.
 * </p>
 */

@Data
public class UserDetailsDTO {

    private long id;

    @NotEmpty(message = ErrorConstants.EMAIL_NOT_NULL)
    private String username;

    @NotEmpty(message = ErrorConstants.LAST_NAME_NOT_NULL)
    private String firstName;

    @NotEmpty(message = ErrorConstants.FIRST_NAME_NOT_NULL)
    private String lastName;

    private String gender;

    private Country country;

    private String countryCode;

    @NotEmpty(message = ErrorConstants.PHONE_NUMBER_NOT_NULL)
    private String phoneNumber;

    private Set<Role> roles = new HashSet<>();

    private Timezone timezone;

    private Long tenantId;

    private Long cultureId;

    /**
     * <p>
     * This function maps a timezone object to a timezone DTO using a ModelMapper.
     * </p>
     *
     * @return {@link TimezoneDTO} object is being returned. The method uses a ModelMapper to map the
     * properties of the timezone object to the corresponding properties of the TimezoneDTO object.
     */
    public TimezoneDTO getTimezone() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(timezone, TimezoneDTO.class);
    }

    /**
     * This function returns a CountryDTO object mapped from a country object using ModelMapper.
     *
     * @return {@link CountryDTO} object is being returned.
     */
    public CountryDTO getCountry() {
        ModelMapper modelMapper = new ModelMapper();
        if (Objects.isNull(country)) {
            return null;
        }
        return modelMapper.map(country, CountryDTO.class);
    }
}
