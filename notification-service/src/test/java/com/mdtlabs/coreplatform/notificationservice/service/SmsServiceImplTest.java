package com.mdtlabs.coreplatform.notificationservice.service;

import java.util.List;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.notificationservice.repository.OutBoundSmsRepository;
import com.mdtlabs.coreplatform.notificationservice.service.impl.SmsServiceImpl;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * Sms Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SmsServiceImplTest {

    @InjectMocks
    private SmsServiceImpl smsService;

    @Mock
    private OutBoundSmsRepository outBoundSmsRepository;

    @Test
    void saveOutBoundSms() {
        //given
        SmsDTO smsDTO = TestDataProvider.getSmsDTO();
        OutBoundSMS outBoundSms = new OutBoundSMS();
        smsDTO.setBody("testBody");
        smsDTO.setToPhoneNo("9090909090");
        smsDTO.setUserName("testUserName");
        smsDTO.setNotificationId(1L);
        smsDTO.setTenantId(1L);
        outBoundSms.setBody(smsDTO.getBody());
        outBoundSms.setPhoneNumber(smsDTO.getToPhoneNo());
        outBoundSms.setUserName(smsDTO.getUserName());
        outBoundSms.setNotificationId(smsDTO.getNotificationId());
        outBoundSms.setTenantId(smsDTO.getTenantId());
        List<OutBoundSMS> outBoundSMSList = List.of(outBoundSms);
        List<SmsDTO> smsDTOList = List.of(smsDTO);

        //when
        when(outBoundSmsRepository.saveAll(outBoundSMSList)).thenReturn(outBoundSMSList);

        //then
        boolean result = smsService.saveOutBoundSms(smsDTOList);
        Assertions.assertEquals(Constants.BOOLEAN_TRUE, result);
    }

    @Test
    void saveOutBoundSmsWithNull() {
        //given
        SmsDTO smsDTO = TestDataProvider.getSmsDTO();
        OutBoundSMS outBoundSms = new OutBoundSMS();
        List<OutBoundSMS> outBoundSMSList = List.of(outBoundSms);
        List<SmsDTO> smsDTOList = List.of(smsDTO);

        //when
        when(outBoundSmsRepository.saveAll(outBoundSMSList)).thenReturn(outBoundSMSList);

        //then
        boolean result = smsService.saveOutBoundSms(smsDTOList);
        Assertions.assertEquals(Constants.BOOLEAN_TRUE, result);
    }

    @Test
    void getOutBoundSms() {
        //given
        ReflectionTestUtils.setField(smsService, "smsRetryAttempts", 1);
        OutBoundSMS outBoundSms = new OutBoundSMS();
        outBoundSms.setBody("testBody");
        outBoundSms.setPhoneNumber("9090909090");
        outBoundSms.setUserName("testUserName");
        outBoundSms.setNotificationId(1L);
        outBoundSms.setTenantId(1L);
        List<OutBoundSMS> outBoundSMSList = List.of(outBoundSms);

        //when
        when(outBoundSmsRepository.getAllSms(1)).thenReturn(outBoundSMSList);

        //then
        List<OutBoundSMS> result = smsService.getOutBoundSms();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.get(0).getTenantId());
        Assertions.assertNull(result.get(0).getFormDataId());
    }

    @Test
    void updateOutBoundSms() {
        //given
        OutBoundSMS outBoundSms = new OutBoundSMS();
        outBoundSms.setBody("testBody");
        outBoundSms.setPhoneNumber("9090909090");
        outBoundSms.setUserName("testUserName");
        outBoundSms.setNotificationId(1L);
        outBoundSms.setTenantId(1L);
        List<OutBoundSMS> outBoundSMSList = List.of(outBoundSms);

        //when
        when(outBoundSmsRepository.saveAll(outBoundSMSList)).thenReturn(outBoundSMSList);

        //then
        List<OutBoundSMS> result = smsService.updateOutBoundSms(outBoundSMSList);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.get(0).getTenantId());
        Assertions.assertNull(result.get(0).getFormDataId());
    }
}