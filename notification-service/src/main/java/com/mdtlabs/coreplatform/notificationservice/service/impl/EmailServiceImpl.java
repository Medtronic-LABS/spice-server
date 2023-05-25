package com.mdtlabs.coreplatform.notificationservice.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.Notification;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;
import com.mdtlabs.coreplatform.notificationservice.repository.EmailTemplateRepository;
import com.mdtlabs.coreplatform.notificationservice.repository.OutBoundEmailRepository;
import com.mdtlabs.coreplatform.notificationservice.service.EmailService;

/**
 * <p>
 * EmailServiceImpl class implements an email service, with methods for sending emails, retrieving email templates,
 * creating and updating outbound emails, and retrieving information about a service instance
 * using a discovery client.
 * </p>
 *
 * @author VigneshKumar created on July 22, 2022
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private DiscoveryClient discoveryClient;

    private RestTemplate restService = new RestTemplate();

    @Value("${app.sendgrid-apikey}")
    private String sendgridApikey;

    @Value("${app.mail-from}")
    private String mailFrom;

    @Value("${app.mail-host}")
    private String mailHost;

    @Value("${app.mail-port}")
    private String mailPort;

    @Value("${app.mail-user}")
    private String mailUser;

    @Value("${app.mail-password}")
    private String mailPassword;

    @Value("${app.url}")
    private String appUrl;

    @Value("${app.mail-send}")
    private String mailSend;

    @Value("${app.email-retry-attempts}")
    private int emailRetryAttempts;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private OutBoundEmailRepository outBoundEmailRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sendEmailUsingStmp(EmailDTO emailDto) {
        try {
            if (mailSend.equalsIgnoreCase(Constants.TRUE)) {
                Properties properties = new Properties();
                properties.put(Constants.MAIL_SMTP_AUTH, Constants.TRUE);
                properties.put(Constants.MAIL_SMTP_STARTTLS_ENABLE, Constants.TRUE);
                properties.put(Constants.MAIL_SMTP_HOST, mailHost);
                properties.put(Constants.MAIL_SMTP_PORT, mailPort);
                properties.put(Constants.MAIL_SMTP_SSL_PROTOCOLS, Constants.SMTP_SSL_PROTOCOL);

                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailUser, mailPassword);
                    }
                };
                Session session = Session.getInstance(properties, auth);
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addHeader(Constants.CONTENT_TYPE, Constants.TEXT_HTML_CHARSET);
                mimeMessage.addHeader(Constants.FORMAT, Constants.FLOWED);
                mimeMessage.addHeader(Constants.CONTENT_TRANSFER_ENCODING, Constants.ENCODING);
                mimeMessage.setFrom(mailFrom);
                mimeMessage.setReplyTo(InternetAddress.parse(mailFrom, false));
                mimeMessage.setSubject(emailDto.getSubject(), Constants.UTF_8);
                mimeMessage.setContent(emailDto.getBody(), Constants.TEXT_HTML_CHARSET);
                mimeMessage.setSentDate(new Date());
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress
                        .parse(StringUtils.isNotBlank(emailDto.getTo())
                                ? emailDto.getTo() : Constants.EMPTY, false));
                mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress
                        .parse(StringUtils.isNotBlank(emailDto.getCc())
                                ? emailDto.getCc() : Constants.EMPTY, false));
                mimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress
                        .parse(StringUtils.isNotBlank(emailDto.getBcc())
                                ? emailDto.getBcc() : Constants.EMPTY, false));
                Transport.send(mimeMessage);
            }
            return true;
        } catch (Exception e) {
            Logger.logError(e);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailTemplate getEmailTemplate(String type, String appType) {
        return emailTemplateRepository.getTemplate(type, appType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createNotificationAndSendEmail(EmailDTO email, String sendStatus) {
        try {
            Notification notification = new Notification(email.getSubject(), email.getBody(),
                    email.getToMails());
            notification.setStatus(Constants.ERROR.equalsIgnoreCase(sendStatus)
                    ? Constants.NOTIFICATION_STATUS_FAILED : Constants.NOTIFICATION_STATUS_PROCESSED);
            String emailIp = getNotificationInfo() + "/notification-service/notification";
            HttpHeaders header = new HttpHeaders();
            HttpEntity<Notification> notificationEntity = new HttpEntity<>(notification, header);
            header.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Map> notificationResponse = restService.exchange(emailIp, HttpMethod.POST,
                    notificationEntity, Map.class);
            Map status = notificationResponse.getBody();
            if (Objects.nonNull(status) && !Objects.isNull(status.get(Constants.ENTITY))) {
                return Constants.SUCCESS;
            }
            return Constants.ERROR;
        } catch (Exception error) {
            Logger.logError(error);
            return Constants.ERROR;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sendEmailNotification(EmailDTO email) {
        return sendEmailUsingStmp(email);
    }

    /**
     * {@inheritDoc}
     */
    public OutBoundEmail createOutBoundEmail(OutBoundEmail outBoundEmail) {
        return outBoundEmailRepository.save(outBoundEmail);
    }

    /**
     * {@inheritDoc}
     */
    public List<OutBoundEmail> updateOutBoundEmails(List<OutBoundEmail> outBoundEmails) {
        return outBoundEmailRepository.saveAll(outBoundEmails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OutBoundEmail> getOutBoundEmails() {
        return outBoundEmailRepository.getAllMail(emailRetryAttempts);
    }

    /**
     * <p>
     * This method is used to retrieve the URI of a service instance named "NOTIFICATION" using a discovery client.
     * </p>
     *
     * @return {@link String} The URI of the first instance of a service named "NOTIFICATION"
     * obtained from the discovery client is returned
     */
    private String getNotificationInfo() {
        String ipInfo = Constants.EMPTY;
        ServiceInstance instance = null;
        try {
            List<ServiceInstance> instanceList = discoveryClient.getInstances(Constants.NOTIFICATION);
            if (!instanceList.isEmpty()) {
                instance = instanceList.get(Constants.ZERO);
            }
            if (null != instance) {
                ipInfo = instance.getUri().toString();

            }
        } catch (Exception e) {
            e.getMessage();
        }
        return ipInfo;
    }
}
