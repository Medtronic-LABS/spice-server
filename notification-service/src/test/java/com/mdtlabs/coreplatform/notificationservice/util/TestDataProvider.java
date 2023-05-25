package com.mdtlabs.coreplatform.notificationservice.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.NotificationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OutBoundEmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.Notification;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.common.model.entity.User;

/**
 * <p>
 * The TestDataProvider class provides static methods to generate test data for various test classes.
 * </p>
 *
 * @author Divya S created on Jan 31, 2023
 */
public class TestDataProvider {

    public static Role getRole() {
        Role role = new Role();
        role.setId(TestConstants.ONE);
        role.setName(Constants.ROLE_ACCOUNT_ADMIN);
        role.setLevel(TestConstants.SITE_LEVEL);
        return role;
    }

    public static Organization getOrganization() {
        Organization organization = new Organization();
        organization.setId(TestConstants.ONE);
        organization.setFormName(Constants.FORM_NAME);
        organization.setFormDataId(TestConstants.ONE);
        organization.setName(TestConstants.COUNTRY_NAME);
        organization.setSequence(TestConstants.ZERO);
        organization.setTenantId(TestConstants.ONE);
        organization.setParentOrganizationId(TestConstants.FIVE);
        organization.setFormDataId(TestConstants.ONE);
        organization.setActive(Boolean.TRUE);
        return organization;
    }

    public static Set<Organization> getSetOrganizations() {
        Set<Organization> setOrganizations = new HashSet<>();
        setOrganizations.add(getOrganization());
        return setOrganizations;
    }

    public static User getUser() {
        User user = new User();
        user.setId(TestConstants.ONE);
        user.setPassword(Constants.USER_DATA);
        user.setFirstName(TestConstants.USER_FIRST_NAME);
        user.setLastName(TestConstants.USER_LAST_NAME);
        user.setPhoneNumber(TestConstants.PHONE_NUMBER);
        user.setUsername(TestConstants.USER_NAME);
        user.setForgetPasswordCount(Constants.ONE);
        user.setForgetPasswordTime(new Date());
        user.setInvalidLoginAttempts(Constants.ONE);
        user.setTenantId(TestConstants.FIVE);
        user.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        user.setRoles(Set.of(getRole()));
        user.setOrganizations(getSetOrganizations());
        return user;
    }

    public static EmailDTO getEmailDTO() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setBcc(Constants.EMAIL);
        emailDTO.setTo(getUser().getUsername());
        emailDTO.setFormDataId(getUser().getId());
        emailDTO.setFormName(Constants.EMAIL_FORM_USER);
        emailDTO.setSubject(Constants.SUCCESS);
        emailDTO.setToMails(TestConstants.USER_NAME);
        emailDTO.setBody(Constants.EMAIL);
        return emailDTO;
    }

    public static OutBoundEmail getOutBoundEmail() {
        OutBoundEmail outBoundEmail = new OutBoundEmail();
        outBoundEmail.setBcc(Constants.EMAIL_FORM_USER);
        outBoundEmail.setBody(Constants.EMAIL);
        outBoundEmail.setRetryAttempts(Constants.TWO);
        outBoundEmail.setTo(TestConstants.USER_NAME);
        outBoundEmail.setCc(Constants.EMAIL);
        outBoundEmail.setSubject(Constants.EMAIL);
        return outBoundEmail;
    }

    public static List<OutBoundEmail> getOutBoundEmails() {
        OutBoundEmail outBoundEmail = getOutBoundEmail();
        return List.of(outBoundEmail);
    }

    public static EmailTemplate getEmailTemplate() {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setId(TestConstants.ONE);
        emailTemplate.setType(Constants.TYPE);
        return emailTemplate;
    }

    public static OutBoundSMS getOutBoundSMS() {
        OutBoundSMS outBoundSMS = new OutBoundSMS();
        return outBoundSMS;
    }


    public static OutBoundEmailDTO getOutBoundEmailDTO() {
        OutBoundEmailDTO emailDTO = new OutBoundEmailDTO();
        return emailDTO;
    }


    public static NotificationDTO getNotificationDTO() {
        NotificationDTO notificationDTO = new NotificationDTO();
        return notificationDTO;
    }

    public static Notification getNotification() {
        Notification notification = new Notification();
        return notification;
    }

    public static SmsDTO getSmsDTO() {
        SmsDTO smsDTO = new SmsDTO();
        return smsDTO;
    }

    public static SMSTemplate getSmsTemplate() {
        SMSTemplate smsTemplate = new SMSTemplate();
        return smsTemplate;
    }

    public static List<OutBoundSMS> getOutboundSmsList() {
        List<OutBoundSMS> smsList = new ArrayList<>();
        smsList.add(getOutBoundSMS());
        return smsList;
    }
}
