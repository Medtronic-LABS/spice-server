package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.dto.TimezoneDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;


/**
 * <p>
 * This class is an POJO for representing users under an Organization.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class UserOrganizationDTO {
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

    private Timezone timezone;

    public TimezoneDTO getTimezone() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(timezone, TimezoneDTO.class);
    }

    /**
     * <p>
     * This function returns a CountryDTO object mapped from a country object using ModelMapper.
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

}
