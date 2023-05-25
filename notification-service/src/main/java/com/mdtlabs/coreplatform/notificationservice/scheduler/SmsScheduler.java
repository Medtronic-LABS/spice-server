package com.mdtlabs.coreplatform.notificationservice.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.notificationservice.service.SmsService;

/**
 * <p>
 * SmsScheduler class schedules and sends SMS notifications using either Twilio API or AWS SNS
 * and updates the status of the sent messages.
 * </p>
 *
 * @author Karthick Murugesan
 */
@EnableScheduling
@Configuration
@ConditionalOnProperty(name = "spring.enable.scheduling.sms")
public class SmsScheduler {

    @Autowired
    private SmsService smsService;

    @Value("${app.twilio-account-sid}")
    private String twilioAccountSid;

    @Value("${app.twilio-auth-token}")
    private String twilioAuthToken;

    @Value("${app.twilio-from-phoneno}")
    private String twilioFromPhoneNumber;

    @Value("${app.enable-sms-notification-twilio}")
    private String enableSMSNotificationTwilio;

    @Value("${app.enable-sms-notification-sns}")
    private String enableSMSNotificationSNS;

    @Value("${app.aws-access-key}")
    private String awsAccessKey;

    @Value("${app.aws-secret-key}")
    private String awsSecretKey;

    @Value("${app.aws-region}")
    private String serverRegion;

    /**
     * <p>
     * This method is used to send SMS notifications using Twilio API based on a scheduled cron expression
     * and to update the status of the sent messages.
     * </p>
     */
    @Scheduled(cron = "${scheduler.cron.sms}")
    @SchedulerLock(name = "TaskScheduler_sms",
            lockAtLeastForString = "${app.shedlock.sms.start}", lockAtMostForString = "${app.shedlock.sms.stop}")
    public void scheduleTaskUsingCronExpression() {
        if (Boolean.parseBoolean(enableSMSNotificationTwilio)) {
            List<OutBoundSMS> smsList = smsService.getOutBoundSms();
            List<OutBoundSMS> smsListToUpdate = new ArrayList<>();
            if (!smsList.isEmpty()) {
                Twilio.init(twilioAccountSid, twilioAuthToken);
                for (OutBoundSMS outBoundSms : smsList) {
                    Message message = Message.creator(new PhoneNumber(outBoundSms.getPhoneNumber()),
                            new PhoneNumber(twilioFromPhoneNumber), outBoundSms.getBody()).create();
                    if (!Objects.isNull(message.getSid())) {
                        outBoundSms.setProcessed(true);
                    } else {
                        outBoundSms.setRetryAttempts(outBoundSms.getRetryAttempts() + 1);
                    }
                    smsListToUpdate.add(outBoundSms);
                }
                smsService.updateOutBoundSms(smsListToUpdate);

            }
        }
    }

    /**
     * <p>
     * This method is used to send SMS notifications using AWS SNS and to update the status of the sent messages.
     * </p>
     */
    @Scheduled(cron = "${scheduler.cron.sms}")
    @SchedulerLock(name = "TaskScheduler_sms",
            lockAtLeastForString = "${app.shedlock.sms.start}", lockAtMostForString = "${app.shedlock.sms.stop}")
    public void sendSMS() {
        if (Boolean.parseBoolean(enableSMSNotificationSNS)) {
            List<OutBoundSMS> smsList = smsService.getOutBoundSms();
            List<OutBoundSMS> smsListToUpdate = new ArrayList<>();
            if (!smsList.isEmpty()) {
                for (OutBoundSMS outBoundSms : smsList) {
                    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
                    AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(serverRegion)
                            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
                    PublishRequest request = new PublishRequest();
                    request.withMessage(outBoundSms.getBody()).withPhoneNumber(outBoundSms.getPhoneNumber());
                    PublishResult publishResult = snsClient.publish(request);
                    if (!Objects.isNull(publishResult.getMessageId())) {
                        outBoundSms.setProcessed(true);
                    } else {
                        outBoundSms.setRetryAttempts(outBoundSms.getRetryAttempts() + 1);
                    }
                    smsListToUpdate.add(outBoundSms);
                }
                smsService.updateOutBoundSms(smsListToUpdate);

            }
        }
    }
}
