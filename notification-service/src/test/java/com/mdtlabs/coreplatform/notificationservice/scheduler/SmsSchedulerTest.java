package com.mdtlabs.coreplatform.notificationservice.scheduler;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.notificationservice.service.SmsService;
import com.mdtlabs.coreplatform.notificationservice.util.TestConstants;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * This is a test class for the SmsScheduler class, which tests various methods related to sending SMS notifications
 * using different services.
 * </p>
 *
 * @author Divya S created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SmsSchedulerTest {

    @InjectMocks
    SmsScheduler smsScheduler;

    @Mock
    SmsService smsService;

    @Test
    void sendEmailUsingSmtp() {
        ReflectionTestUtils.setField(smsScheduler, "enableSMSNotificationTwilio", "true");
        when(smsService.getOutBoundSms()).thenReturn(List.of());
        smsScheduler.scheduleTaskUsingCronExpression();
        verify(smsService, atLeastOnce()).getOutBoundSms();
    }

    @Test
    void sendEmailUsingSmtpFalse() {
        ReflectionTestUtils.setField(smsScheduler, "enableSMSNotificationTwilio", "false");
        when(smsService.getOutBoundSms()).thenReturn(List.of());
        smsScheduler.scheduleTaskUsingCronExpression();
        verify(smsService, times(0)).getOutBoundSms();
    }

    @Test
    void sendSmsTest() {
        ReflectionTestUtils.setField(smsScheduler, "enableSMSNotificationSNS", "true");
        when(smsService.getOutBoundSms()).thenReturn(List.of());
        smsScheduler.sendSMS();
        verify(smsService, times(1)).getOutBoundSms();
    }

    @Test
    void sendSmsTestFalse() {
        ReflectionTestUtils.setField(smsScheduler, "enableSMSNotificationSNS", "false");
        when(smsService.getOutBoundSms()).thenReturn(List.of());
        smsScheduler.sendSMS();
        verify(smsService, times(0)).getOutBoundSms();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"123"})
    void scheduleTaskUsingCronExpression(String id) {
        ReflectionTestUtils.setField(smsScheduler, "enableSMSNotificationTwilio", "true");
        ReflectionTestUtils.setField(smsScheduler, "twilioAccountSid", "3");
        ReflectionTestUtils.setField(smsScheduler, "twilioAuthToken", "token");
        ReflectionTestUtils.setField(smsScheduler, "twilioFromPhoneNumber", "9876555666");

        MockedStatic<Message> message = mockStatic(Message.class);
        Message mockMessage = mock(Message.class);
        MessageCreator creator = mock(MessageCreator.class);
        List<OutBoundSMS> smsList = TestDataProvider.getOutboundSmsList();
        smsList.get(0).setBody(Constants.EMAIL);
        smsList.get(0).setPhoneNumber(TestConstants.PHONE_NUMBER);
        smsList.get(0).setProcessed(true);
        smsList.get(0).setRetryAttempts(2);
        OutBoundSMS outBoundSMS = smsList.get(0);
        outBoundSMS.setRetryAttempts(3);
        List<OutBoundSMS> smsListToUpdate = new ArrayList<>();
        List<OutBoundSMS> secondSmsListToUpdate = new ArrayList<>();
        smsListToUpdate.add(smsList.get(0));
        secondSmsListToUpdate.add(outBoundSMS);

        //when
        when(smsService.getOutBoundSms()).thenReturn(smsList);
        message.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString()))
                .thenReturn(creator);
        when(creator.create()).thenReturn(mockMessage);
        when(mockMessage.getSid()).thenReturn(id);
        when(smsService.updateOutBoundSms(smsListToUpdate)).thenReturn(smsListToUpdate);
        when(smsService.updateOutBoundSms(secondSmsListToUpdate)).thenReturn(secondSmsListToUpdate);

        //then
        smsScheduler.scheduleTaskUsingCronExpression();
        verify(smsService, atLeastOnce()).getOutBoundSms();
        message.close();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"123"})
    void sendSms(String id) {
        ReflectionTestUtils.setField(smsScheduler, "enableSMSNotificationSNS", "true");
        ReflectionTestUtils.setField(smsScheduler, "awsAccessKey", "awsKey");
        ReflectionTestUtils.setField(smsScheduler, "awsSecretKey", "awsSecretKey");
        ReflectionTestUtils.setField(smsScheduler, "serverRegion", "us-east-1");

        MockedStatic<AmazonSNSClientBuilder> amazonSNSClientBuilder = mockStatic(AmazonSNSClientBuilder.class);
        AmazonSNSClientBuilder amazonBuilder = mock(AmazonSNSClientBuilder.class);
        AmazonSNS amazonSNS = mock(AmazonSNS.class);
        PublishResult publishResult = mock(PublishResult.class);
        List<OutBoundSMS> smsList = TestDataProvider.getOutboundSmsList();
        smsList.get(0).setBody(Constants.EMAIL);
        smsList.get(0).setPhoneNumber(TestConstants.PHONE_NUMBER);
        smsList.get(0).setProcessed(true);
        smsList.get(0).setRetryAttempts(2);
        OutBoundSMS outBoundSMS = smsList.get(0);
        outBoundSMS.setRetryAttempts(3);
        List<OutBoundSMS> smsListToUpdate = new ArrayList<>();
        List<OutBoundSMS> secondSmsListToUpdate = new ArrayList<>();
        smsListToUpdate.add(smsList.get(0));
        secondSmsListToUpdate.add(outBoundSMS);

        //when
        when(smsService.getOutBoundSms()).thenReturn(smsList);
        amazonSNSClientBuilder.when(AmazonSNSClientBuilder::standard).thenReturn(amazonBuilder);
        when(amazonBuilder.withRegion("us-east-1")).thenReturn(amazonBuilder);
        when(amazonBuilder.withCredentials(any(AWSStaticCredentialsProvider.class))).thenReturn(amazonBuilder);
        when(amazonBuilder.build()).thenReturn(amazonSNS);
        when(amazonSNS.publish(any(PublishRequest.class))).thenReturn(publishResult);
        when(publishResult.getMessageId()).thenReturn(id);
        when(smsService.updateOutBoundSms(smsListToUpdate)).thenReturn(smsListToUpdate);
        when(smsService.updateOutBoundSms(secondSmsListToUpdate)).thenReturn(secondSmsListToUpdate);

        //then
        smsScheduler.sendSMS();
        verify(smsService, times(1)).getOutBoundSms();
        amazonSNSClientBuilder.close();
    }
}
