package com.mdtlabs.coreplatform.userservice.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserSuperAdminDto;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.StringUtil;
import com.mdtlabs.coreplatform.userservice.util.TestConstants;
import com.mdtlabs.coreplatform.userservice.util.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * User Mapper Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on feb 23, 2023
 */
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void testSetExistingUser() {
        //given
        User user = TestDataProvider.getUser();
        User existingUser = TestDataProvider.getUser();

        //then
        userMapper.setExistingUser(user, existingUser);
        assertNotNull(user);
    }

    @Test
    void testSetUserCreationEmailTemplate() {
        //given
        User user = TestDataProvider.getUser();
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setBody(Constants.APP_URL_EMAIL);
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        emailDTO.setSubject(Constants.RESET_NOTIFICATION_SUBJECT);
        Map<String, String> data = Map.of(Constants.EMAIL, Constants.EMAIL_FORM_USER);
        emailDTO.setEmailTemplate(emailTemplate);
        emailDTO.setBody(StringUtil.parseEmailTemplate(emailTemplate.getBody(), data));

        //then
        EmailDTO actualEmailDto = userMapper.setUserCreationEmailTemplate(user, emailTemplate, emailDTO, data);
        assertNotNull(actualEmailDto);
        assertEquals(emailDTO, actualEmailDto);
    }

    @Test
    void testSetExistingUserWithOrganization() {
        //given
        Organization organization = TestDataProvider.getOrganization();
        User user = TestDataProvider.getUser();
        User existingUser = TestDataProvider.getUser();

        //then
        userMapper.setExistingUser(user, existingUser, organization);
        assertNotNull(user);
    }

    @Test
    void setForgotPasswordEmailTemplate() {
        //given
        User user = TestDataProvider.getUser();
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setBody(Constants.APP_URL_EMAIL);
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        emailDTO.setSubject(Constants.RESET_NOTIFICATION_SUBJECT);
        Map<String, String> data = Map.of(Constants.EMAIL, Constants.EMAIL_FORM_USER);
        emailDTO.setEmailTemplate(emailTemplate);
        emailDTO.setFrom(Constants.EMAIL_FORM_USER);
        emailDTO.setBody(StringUtil.parseEmailTemplate(emailTemplate.getBody(), data));

        //then
        EmailDTO actualEmailDto = userMapper.setForgotPasswordEmailTemplate(emailTemplate,
                Constants.EMAIL_FORM_USER, user, emailDTO, data);
        assertNotNull(actualEmailDto);
        assertEquals(emailDTO, actualEmailDto);
    }

    @ParameterizedTest
    @CsvSource({",,,,", "male, testFirstName, testLastName, 9876543210, +91"})
    void setSuperAdminUser(String gender, String firstName, String lastName, String phoneNumber, String countryCode) {
        //given
        UserSuperAdminDto userSuperAdminDto = TestDataProvider.getUserSuperAdminDto();
        userSuperAdminDto.setFirstName(firstName);
        userSuperAdminDto.setGender(gender);
        userSuperAdminDto.setCountryCode(countryCode);
        userSuperAdminDto.setLastName(lastName);
        userSuperAdminDto.setPhoneNumber(phoneNumber);
        userSuperAdminDto.setTimezone(new Timezone(TestConstants.ONE));
        User user = TestDataProvider.getUser();
        user.setTimezone(new Timezone(TestConstants.ONE));

        //then
        User actualUser = userMapper.setSuperAdminUser(userSuperAdminDto, user);
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }
}