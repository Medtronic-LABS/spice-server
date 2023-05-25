package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.dto.TimezoneDTO;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Date;

/**
 * <p>
 * This is a Java class representing a user with super admin privileges, including personal information
 * and methods to get and set email and timezone.
 * </p>
 */
@Data
public class UserSuperAdminDto {

    private long id;

    private String firstName;

    private String lastName;

    private String gender;

    private long tenantId;

    private String email;

    private String phoneNumber;

    private Date createdAt;

    private Date updatedAt;

    private Timezone timezone;

    private String countryCode;

    private String username;

    /**
     * <p>
     * This function maps a timezone object to a TimezoneDTO object using ModelMapper.
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
     * <p>
     * This function returns the email address which is the same as the username.
     * </p>
     *
     * @return The method is returning the email address which is the same as the username.
     */
    public String getEmail() {
        this.email = this.username;
        return this.email;
    }

    /**
     * <p>
     * This function sets the email property of an object to a given value.
     * </p>
     *
     * @param email The parameter "email" is a string that represents the email address of a user. The
     *              method "setEmail" sets the value of the email address for the current object instance.
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
