package com.mdtlabs.coreplatform.notificationservice.controller;

import java.util.List;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.notificationservice.service.SmsService;
import com.mdtlabs.coreplatform.notificationservice.service.SmsTemplateService;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * SMS Controller Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
class SmsControllerTest {

    @InjectMocks
    private SmsController smsController;

    @Mock
    private SmsService smsService;

    @Mock
    private SmsTemplateService templateService;

    @Test
    void saveOutBoundSms() {
        //given
        List<SmsDTO> smsDataList = List.of(TestDataProvider.getSmsDTO());

        //when
        when(smsService.saveOutBoundSms(smsDataList)).thenReturn(Constants.BOOLEAN_TRUE);

        //then
        SuccessResponse<String> successResponse = smsController.saveOutBoundSms(smsDataList);
        Assertions.assertNotNull(successResponse);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void getSmsTemplateValues() {
        //given
        SMSTemplate smsTemplate = TestDataProvider.getSmsTemplate();

        //when
        when(templateService.getSmsTemplateValues(Constants.TEMPLATE_TYPE_ENROLL_PATIENT)).thenReturn(smsTemplate);

        //then
        ResponseEntity<SMSTemplate> response = smsController.getSmsTemplateValues(Constants
                .TEMPLATE_TYPE_ENROLL_PATIENT);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void saveOutBoundSmsValues() {
        //given
        OutBoundSMS outBoundSMS = TestDataProvider.getOutBoundSMS();
        outBoundSMS.setId(1L);
        List<OutBoundSMS> outBoundSMSList = List.of(outBoundSMS);

        //when
        when(smsService.updateOutBoundSms(outBoundSMSList)).thenReturn(outBoundSMSList);

        //then
        List<OutBoundSMS> result = smsController.saveOutBoundSmsValues(outBoundSMSList);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.get(0).getId());
    }
}