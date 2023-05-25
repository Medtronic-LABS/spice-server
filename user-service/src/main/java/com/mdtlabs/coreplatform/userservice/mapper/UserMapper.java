package com.mdtlabs.coreplatform.userservice.mapper;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserSuperAdminDto;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.StringUtil;

/**
 * <p>
 * The UserMapper class contains methods for updating and setting properties of User objects, as well as setting email
 * templates for user creation and forgot password emails.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Component
public class UserMapper {

    /**
     * <p>
     * This method is used to add an organization to an existing user's list of organizations.
     * </p>
     *
     * @param user         {@link User} The user that needs to be updated or modified is given
     * @param existingUser {@link User} The existing user that needs to be updated with the new organization is given
     * @param organization {@link Organization} It represents an organization that a user can belong to is given
     */
    public void setExistingUser(User user, User existingUser, Organization organization) {
        setExistingUser(user, existingUser);
        existingUser.getOrganizations().add(organization);
    }

    /**
     * <p>
     * This method is used to update the properties of an existing user object with the properties of another user object.
     * </p>
     *
     * @param user         {@link User} The user that needs to be updated or modified is given
     * @param existingUser {@link User} The existing user that needs to be updated with the new information is given
     */
    public void setExistingUser(User user, User existingUser) {
        existingUser.setTimezone(user.getTimezone());
        existingUser.setGender(user.getGender());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setCountryCode(user.getCountryCode());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setCultureId(user.getCultureId());
    }

    /**
     * <p>
     * This method is used to set the email template for user creation and populates it with user data.
     * </p>
     *
     * @param user          {@link User} The user for whom the forgot password email is being created is given
     * @param emailTemplate {@link EmailTemplate} The email template to be used for the email is given
     * @param emailDto      {@link EmailDTO} The EmailDTO that represents the email message being
     *                      constructed and sent is given
     * @param data          {@link Map}  A map containing key-value pairs of data that will be used to replace placeholders in the email
     *                      template is given
     * @return {@link EmailDTO} The EmailDTO is returned after setting the values from given details
     */
    public EmailDTO setUserCreationEmailTemplate(User user, EmailTemplate emailTemplate, EmailDTO emailDto,
                                                 Map<String, String> data) {
        emailDto.setBody(StringUtil.parseEmailTemplate(emailTemplate.getBody(), data));
        emailDto.setEmailTemplate(emailTemplate);
        emailDto.setSubject(emailTemplate.getSubject());
        emailDto.setTo(user.getUsername());
        emailDto.setFormDataId(user.getId());
        emailDto.setFormName(Constants.EMAIL_FORM_USER);
        return emailDto;
    }

    /**
     * <p>
     * This method is used to set the email template for a forgot password email and populates it with data
     * before returning the EmailDTO.
     * </p>
     *
     * @param emailTemplate {@link EmailTemplate} The email template to be used for the
     *                      forgot password email is given
     * @param mailUser      {@link String} The name of the user who is sending the email is given
     * @param user          {@link User} The user for whom the forgot password email is being sent is given
     * @param emailDto      {@link EmailDTO} The EmailDTO that represents the email message being
     *                      constructed and sent is given
     * @param data          {@link Map} A map containing key-value pairs of data that will be used to replace
     *                      placeholders in the email template is given
     * @return {@link EmailDTO} The EmailDTO is returned after setting the values from given details
     */
    public EmailDTO setForgotPasswordEmailTemplate(EmailTemplate emailTemplate, String mailUser, User user,
                                                   EmailDTO emailDto, Map<String, String> data) {
        emailDto.setBody(StringUtil.parseEmailTemplate(emailTemplate.getBody(), data));
        emailDto.setFromName(mailUser);
        emailDto.setSubject(emailTemplate.getSubject());
        emailDto.setTo(user.getUsername());
        emailDto.setFrom(mailUser);
        emailDto.setFormDataId(user.getId());
        emailDto.setFormName(Constants.EMAIL_FORM_USER);
        return emailDto;
    }

    /**
     * <p>
     * This method is used to set the properties of a User based on the values provided in a
     * UserSuperAdminDto.
     * </p>
     *
     * @param userDto {@link UserSuperAdminDto} It contains the updated information for the user
     * @param user    {@link User} The user that needs to be updated is given
     * @return {@link User} The User after setting the properties of UserSuperAdminDto for the
     * given conditions is returned
     */
    public User setSuperAdminUser(UserSuperAdminDto userDto, User user) {
        if (!Objects.isNull(userDto.getGender())) {
            user.setGender(userDto.getGender());
        }
        if (!Objects.isNull(userDto.getFirstName())) {
            user.setFirstName(userDto.getFirstName());
        }
        if (!Objects.isNull(userDto.getLastName())) {
            user.setLastName(userDto.getLastName());
        }
        if (!Objects.isNull(userDto.getPhoneNumber())) {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
        if (!Objects.isNull(userDto.getCountryCode())) {
            user.setCountryCode(userDto.getCountryCode());
        }
        if (!Objects.isNull(userDto.getTimezone())) {
            user.setTimezone(new Timezone(userDto.getTimezone().getId()));
        }
        return user;
    }
}
