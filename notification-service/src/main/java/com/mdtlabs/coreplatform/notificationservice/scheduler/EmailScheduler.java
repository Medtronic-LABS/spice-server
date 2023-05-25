package com.mdtlabs.coreplatform.notificationservice.scheduler;

import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;
import com.mdtlabs.coreplatform.notificationservice.service.EmailService;


/**
 * <p>
 * EmailScheduler class schedules and sends out emails based on a cron
 * expression and updates the status of the sent emails.
 * </p>
 *
 * @author Karthick Murugesan
 */
@EnableScheduling
@Configuration
@ConditionalOnProperty(name = "spring.enable.scheduling.email")
public class EmailScheduler {

    @Autowired
    private EmailService emailService;

    @Value("${app.mail-send}")
    private String enableEmailNotification;

    /**
     * <p>
     * This method is used to send out emails based on a cron expression and to update the status of the sent emails.
     * </p>
     */
    @Scheduled(cron = "${scheduler.cron.email}")
    @SchedulerLock(name = "TaskScheduler_email",
            lockAtLeastForString = "${app.shedlock.email.start}", lockAtMostForString = "${app.shedlock.email.stop}")
    public void scheduleTaskUsingCronExpression() {
        if (Boolean.parseBoolean(enableEmailNotification)) {
            List<OutBoundEmail> mails = emailService.getOutBoundEmails();
            List<OutBoundEmail> emailsListToUpdate = new ArrayList<>();
            if (!mails.isEmpty()) {
                for (OutBoundEmail outBoundEmail : mails) {
                    EmailDTO mail = new EmailDTO(outBoundEmail.getTo(), outBoundEmail.getCc(), outBoundEmail.getBcc(),
                            outBoundEmail.getSubject(), outBoundEmail.getBody());
                    boolean isSent = emailService.sendEmailNotification(mail);
                    if (isSent) {
                        outBoundEmail.setProcessed(isSent);
                    } else {
                        outBoundEmail.setRetryAttempts(outBoundEmail.getRetryAttempts() + 1);
                    }
                    emailsListToUpdate.add(outBoundEmail);
                }
                emailService.updateOutBoundEmails(emailsListToUpdate);
            }
        }
    }
}
