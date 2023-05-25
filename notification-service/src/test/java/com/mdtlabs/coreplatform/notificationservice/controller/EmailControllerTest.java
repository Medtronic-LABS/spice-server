package com.mdtlabs.coreplatform.notificationservice.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OutBoundEmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.notificationservice.service.EmailService;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * Email Controller Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void sendEmailNotification() {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();

        //when
        when(emailService.sendEmailNotification(emailDTO)).thenReturn(Constants.BOOLEAN_TRUE);

        //then
        SuccessResponse<Boolean> successResponse = emailController.sendEmailNotification(emailDTO);
        Assertions.assertNotNull(successResponse);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void getEmailTemplate() {
        //given
        EmailTemplate emailTemplate = TestDataProvider.getEmailTemplate();

        //when
        when(emailService.getEmailTemplate(Constants.TYPE, Constants.APP_TYPE)).thenReturn(emailTemplate);

        //then
        ResponseEntity<EmailTemplate> response = emailController.getEmailTemplate(Constants.TYPE, Constants.APP_TYPE);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createOutBoundEmail() {
        //given
        OutBoundEmail outBoundEmail = TestDataProvider.getOutBoundEmail();
        OutBoundEmailDTO emailDTO = TestDataProvider.getOutBoundEmailDTO();

        //when
        when(modelMapper.map(emailDTO, OutBoundEmail.class)).thenReturn(outBoundEmail);
        when(emailService.createOutBoundEmail(outBoundEmail)).thenReturn(outBoundEmail);

        //then
        ResponseEntity<Boolean> response = emailController.createOutBoundEmail(emailDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createOutBoundEmailWithNull() {
        //given
        OutBoundEmail outBoundEmail = TestDataProvider.getOutBoundEmail();
        OutBoundEmailDTO emailDTO = TestDataProvider.getOutBoundEmailDTO();

        //when
        when(modelMapper.map(emailDTO, OutBoundEmail.class)).thenReturn(outBoundEmail);
        when(emailService.createOutBoundEmail(outBoundEmail)).thenReturn(null);

        //then
        ResponseEntity<Boolean> response = emailController.createOutBoundEmail(emailDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void healthCheck() {
        //then
        SuccessResponse<Boolean> successResponse = emailController.healthCheck();
        Assertions.assertNotNull(successResponse);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }
}