package com.mdtlabs.coreplatform.notificationservice.service;

import java.util.List;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.notificationservice.repository.SmsTemplateRepository;
import com.mdtlabs.coreplatform.notificationservice.service.impl.SmsTemplateServiceImpl;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * Sms Template Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
class SmsTemplateServiceImplTest {

    @InjectMocks
    private SmsTemplateServiceImpl smsTemplateService;

    @Mock
    private SmsTemplateRepository smsTemplateRepository;

    @Test
    void getSmsTemplateValues() {
        //given
        SMSTemplate smsTemplate = TestDataProvider.getSmsTemplate();
        smsTemplate.setId(1);

        //when
        when(smsTemplateRepository.findByType(Constants.TEMPLATE_TYPE_ENROLL_PATIENT)).thenReturn(smsTemplate);

        //then
        SMSTemplate result = smsTemplateService.getSmsTemplateValues(Constants.TEMPLATE_TYPE_ENROLL_PATIENT);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
    }

    @Test
    void getSMSTemplates() {
        //given
        SMSTemplate smsTemplate = TestDataProvider.getSmsTemplate();
        smsTemplate.setId(1);
        List<SMSTemplate> smsTemplates = List.of(smsTemplate);

        //when
        when(smsTemplateRepository.getSMSTemplates(Constants.TEMPLATE_LIST)).thenReturn(smsTemplates);

        //then
        List<SMSTemplate> result = smsTemplateService.getSMSTemplates(List.of(Constants.TEMPLATE_TYPE_ENROLL_PATIENT));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.get(0).getId());
    }
}