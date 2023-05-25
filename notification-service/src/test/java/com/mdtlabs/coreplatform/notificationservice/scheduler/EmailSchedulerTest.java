package com.mdtlabs.coreplatform.notificationservice.scheduler;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;
import com.mdtlabs.coreplatform.notificationservice.service.EmailService;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * This is a test class for the EmailScheduler class that tests the scheduling of email notifications using a cron
 * expression.
 * </p>
 *
 * @author Divya S created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailSchedulerTest {

    @InjectMocks
    EmailScheduler emailScheduler;

    @Mock
    EmailService emailService;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void scheduleTaskUsingCronExpression(boolean isSent) {
        //given
        ReflectionTestUtils.setField(emailScheduler, "enableEmailNotification", "true");
        List<OutBoundEmail> outBoundEmails = TestDataProvider.getOutBoundEmails();
        OutBoundEmail outBoundEmail = TestDataProvider.getOutBoundEmail();
        outBoundEmail.setProcessed(Boolean.TRUE);
        OutBoundEmail secondOutBoundEmail = outBoundEmail;
        secondOutBoundEmail.setRetryAttempts(Constants.NUMBER_THREE);
        EmailDTO emailDTO = new EmailDTO(outBoundEmail.getTo(), outBoundEmail.getCc(), outBoundEmail.getBcc(),
                outBoundEmail.getSubject(), outBoundEmail.getBody());
        List<OutBoundEmail> emailsListToUpdate = new ArrayList<>();
        emailsListToUpdate.add(outBoundEmail);
        List<OutBoundEmail> outBoundEmailList = List.of(secondOutBoundEmail);

        //when
        when(emailService.getOutBoundEmails()).thenReturn(outBoundEmails);
        when(emailService.sendEmailNotification(emailDTO)).thenReturn(isSent);
        when(emailService.updateOutBoundEmails(emailsListToUpdate)).thenReturn(emailsListToUpdate);
        when(emailService.updateOutBoundEmails(outBoundEmailList)).thenReturn(emailsListToUpdate);

        //then
        emailScheduler.scheduleTaskUsingCronExpression();
        verify(emailService, atLeastOnce()).getOutBoundEmails();
        verify(emailService, atLeastOnce()).sendEmailNotification(emailDTO);
    }

    @Test
    void scheduleTaskUsingCronExpressionEmptyMails() {
        //given
        ReflectionTestUtils.setField(emailScheduler, "enableEmailNotification", "true");
        List<OutBoundEmail> outBoundEmails = new ArrayList<>();

        //when
        when(emailService.getOutBoundEmails()).thenReturn(outBoundEmails);

        //then
        emailScheduler.scheduleTaskUsingCronExpression();
        verify(emailService, atLeastOnce()).getOutBoundEmails();
    }

    @Test
    void verifyScheduleTaskUsingCronExpression() {
        //given
        ReflectionTestUtils.setField(emailScheduler, "enableEmailNotification", "false");

        //then
        emailScheduler.scheduleTaskUsingCronExpression();
        verify(emailService, times(0)).getOutBoundEmails();
    }
}