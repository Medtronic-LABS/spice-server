package com.mdtlabs.coreplatform.notificationservice.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.Notification;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;
import com.mdtlabs.coreplatform.notificationservice.repository.EmailTemplateRepository;
import com.mdtlabs.coreplatform.notificationservice.repository.OutBoundEmailRepository;
import com.mdtlabs.coreplatform.notificationservice.service.impl.EmailServiceImpl;
import com.mdtlabs.coreplatform.notificationservice.util.TestConstants;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * This is a test class for the EmailServiceImpl class, containing various test cases for its methods.
 * </p>
 *
 * @author Divya S created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailServiceTest {

    @InjectMocks
    EmailServiceImpl emailService;

    @Mock
    private DiscoveryClient discoveryClient;

    @Mock
    private EmailTemplateRepository emailTemplateRepository;

    @Mock
    private OutBoundEmailRepository outBoundEmailRepository;

    @Mock
    private RestTemplate restService;

    @ParameterizedTest
    @ValueSource(strings = {"value", ""})
    void sendEmailUsingSmtp(String value) {
        //given
        ReflectionTestUtils.setField(emailService, "mailSend", "true");
        ReflectionTestUtils.setField(emailService, "mailUser", "user");
        ReflectionTestUtils.setField(emailService, "mailPassword", "password");
        ReflectionTestUtils.setField(emailService, "mailHost", "host");
        ReflectionTestUtils.setField(emailService, "mailPort", "port");
        ReflectionTestUtils.setField(emailService, "mailFrom", TestConstants.USER_NAME);

        MockedStatic<Session> session = mockStatic(Session.class);
        Session mockSession = mock(Session.class);
        MockedStatic<Transport> transport = mockStatic(Transport.class);
        Properties properties = getProperties();
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        emailDTO.setBcc(value);
        emailDTO.setCc(value);
        emailDTO.setTo(value);
        Authenticator auth = getAuthenticator();

        //when
        session.when(() -> Session.getInstance(properties, auth)).thenReturn(mockSession);
        transport.when(() -> Transport.send(any(MimeMessage.class))).thenAnswer((Answer<Void>) invocation -> null);

        //then
        boolean actualValue = emailService.sendEmailUsingStmp(emailDTO);
        assertTrue(actualValue);
        session.close();
        transport.close();
    }

    @Test
    void sendEmailUsingSmtpWithException() {
        //given
        ReflectionTestUtils.setField(emailService, "mailSend", "true");
        ReflectionTestUtils.setField(emailService, "mailUser", "user");
        ReflectionTestUtils.setField(emailService, "mailPassword", "password");
        ReflectionTestUtils.setField(emailService, "mailHost", "host");
        ReflectionTestUtils.setField(emailService, "mailPort", "port");
        ReflectionTestUtils.setField(emailService, "mailFrom", TestConstants.USER_NAME);
        Authenticator auth = getAuthenticator();
        MockedStatic<Session> session = mockStatic(Session.class);
        Session mockSession = mock(Session.class);
        MockedStatic<Transport> transport = mockStatic(Transport.class);
        Properties properties = getProperties();

        //when
        session.when(() -> Session.getInstance(properties, auth)).thenReturn(mockSession);
        transport.when(() -> Transport.send(any(MimeMessage.class))).thenAnswer((Answer<Void>) invocation -> null);

        //then
        boolean actualValue = emailService.sendEmailUsingStmp(null);
        session.close();
        transport.close();
        assertFalse(actualValue);
    }

    @Test
    void getEmailTemplate() {
        //given
        EmailTemplate emailTemplate = TestDataProvider.getEmailTemplate();

        //when
        when(emailTemplateRepository.getTemplate(Constants.TYPE, Constants.SPICE)).thenReturn(emailTemplate);

        //then
        EmailTemplate template = emailService.getEmailTemplate(Constants.TYPE, Constants.SPICE);
        assertNotNull(template);
        assertEquals(emailTemplate.getId(), template.getId());
        assertEquals(emailTemplate.getType(), template.getType());
    }

    @ParameterizedTest
    @CsvSource({"ERROR, Notification status failed", "SUCCESS, Notification status processed"})
    void createNotificationAndSendEmail(String status, String notificationStatus) throws URISyntaxException {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        ServiceInstance serviceInstance = mock(ServiceInstance.class);
        List<ServiceInstance> serviceInstances = mock(List.class);
        Notification notification = new Notification(emailDTO.getSubject(), emailDTO.getBody(),
                emailDTO.getToMails());
        notification.setStatus(notificationStatus);
        String emailIp = "app_url_email/notification-service/notification";
        HttpHeaders header = new HttpHeaders();
        HttpEntity<Notification> notificationEntity = new HttpEntity<>(notification, header);
        header.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> notificationResponse = new ResponseEntity<>(Map.of(), HttpStatus.OK);

        //when
        when(discoveryClient.getInstances(Constants.NOTIFICATION)).thenReturn(serviceInstances);
        when(serviceInstances.isEmpty()).thenReturn(false);
        when(serviceInstances.get(Constants.ZERO)).thenReturn(serviceInstance);
        when(serviceInstance.getUri()).thenReturn(new URI(Constants.APP_URL_EMAIL));
        when(restService.exchange(emailIp, HttpMethod.POST, notificationEntity, Map.class))
                .thenReturn(notificationResponse);

        //then
        String actualResponse = emailService.createNotificationAndSendEmail(emailDTO, status);
        assertNotNull(actualResponse);
        assertEquals(Constants.ERROR, actualResponse);
    }

    @ParameterizedTest
    @CsvSource({"ERROR, Notification status failed", "SUCCESS, Notification status processed"})
    void testCreateNotificationAndSendEmail(String status, String notificationStatus) {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        Notification notification = new Notification(emailDTO.getSubject(), emailDTO.getBody(),
                emailDTO.getToMails());
        notification.setStatus(notificationStatus);
        String emailIp = "app_url_email/notification-service/notification";
        HttpHeaders header = new HttpHeaders();
        HttpEntity<Notification> notificationEntity = new HttpEntity<>(notification, header);
        header.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> notificationResponse = new ResponseEntity<>(Map.of(), HttpStatus.OK);

        //when
        when(discoveryClient.getInstances(Constants.NOTIFICATION)).thenReturn(new ArrayList<>());
        when(restService.exchange(emailIp, HttpMethod.POST, notificationEntity, Map.class))
                .thenReturn(notificationResponse);

        //then
        String actualResponse = emailService.createNotificationAndSendEmail(emailDTO, status);
        assertNotNull(actualResponse);
        assertEquals(Constants.ERROR, actualResponse);
    }

    @Test
    void sendEmailNotification() {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        ReflectionTestUtils.setField(emailService, "mailSend", "false");

        //then
        boolean actualValue = emailService.sendEmailNotification(emailDTO);
        assertTrue(actualValue);
    }

    @Test
    void createOutBoundEmail() {
        //given
        OutBoundEmail outBoundEmail = TestDataProvider.getOutBoundEmail();

        //when
        when(outBoundEmailRepository.save(outBoundEmail)).thenReturn(outBoundEmail);

        //then
        OutBoundEmail actualOutBoundEmail = emailService.createOutBoundEmail(outBoundEmail);
        assertNotNull(actualOutBoundEmail);
        assertEquals(outBoundEmail.getBcc(), actualOutBoundEmail.getBcc());
        assertEquals(outBoundEmail.getBody(), actualOutBoundEmail.getBody());
    }

    @Test
    void updateOutBoundEmails() {
        //given
        List<OutBoundEmail> outBoundEmails = TestDataProvider.getOutBoundEmails();

        //when
        when(outBoundEmailRepository.saveAll(outBoundEmails)).thenReturn(outBoundEmails);

        //then
        List<OutBoundEmail> expectedOutBoundEmails = emailService.updateOutBoundEmails(outBoundEmails);
        assertNotNull(expectedOutBoundEmails);
        assertFalse(expectedOutBoundEmails.isEmpty());
        assertEquals(outBoundEmails.size(), expectedOutBoundEmails.size());
        assertEquals(outBoundEmails.get(0), expectedOutBoundEmails.get(0));
    }

    @Test
    void getOutBoundEmails() {
        //given
        ReflectionTestUtils.setField(emailService, "emailRetryAttempts", 1);
        List<OutBoundEmail> outBoundEmails = TestDataProvider.getOutBoundEmails();

        //when
        when(outBoundEmailRepository.getAllMail(1)).thenReturn(outBoundEmails);

        //then
        List<OutBoundEmail> expectedOutBoundEmails = emailService.getOutBoundEmails();
        assertNotNull(expectedOutBoundEmails);
        assertFalse(expectedOutBoundEmails.isEmpty());
        assertEquals(outBoundEmails.size(), expectedOutBoundEmails.size());
        assertEquals(outBoundEmails.get(0), expectedOutBoundEmails.get(0));
    }

    @ParameterizedTest
    @CsvSource({"ERROR, Notification status failed, true", "SUCCESS, Notification status processed, false"})
    void createNotificationAndSendEmailSuccess(String status, String notificationStatus, boolean isMapValueExists) throws URISyntaxException {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        ServiceInstance serviceInstance = mock(ServiceInstance.class);
        List<ServiceInstance> serviceInstances = mock(List.class);
        Notification notification = new Notification(emailDTO.getSubject(), emailDTO.getBody(),
                emailDTO.getToMails());
        notification.setStatus(notificationStatus);
        String emailIp = "app_url_email/notification-service/notification";
        HttpHeaders header = new HttpHeaders();
        HttpEntity<Notification> notificationEntity = new HttpEntity<>(notification, header);
        header.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> notificationResponse = new ResponseEntity<>(isMapValueExists ? Map.of(Constants.ENTITY, "SUCCESS") : null, HttpStatus.OK);

        //when
        when(discoveryClient.getInstances(Constants.NOTIFICATION)).thenReturn(serviceInstances);
        when(restService.exchange(emailIp, HttpMethod.POST, notificationEntity, Map.class))
                .thenReturn(notificationResponse);
        when(serviceInstances.isEmpty()).thenReturn(false);
        when(serviceInstances.get(Constants.ZERO)).thenReturn(serviceInstance);
        when(serviceInstance.getUri()).thenReturn(new URI(Constants.APP_URL_EMAIL));

        //then
        String actualResponse = emailService.createNotificationAndSendEmail(emailDTO, status);
        assertNotNull(actualResponse);
        assertEquals(isMapValueExists ? Constants.SUCCESS : Constants.ERROR, actualResponse);
    }


    private Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("user", "password");
            }
        };
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(Constants.MAIL_SMTP_AUTH, Constants.TRUE);
        properties.put(Constants.MAIL_SMTP_STARTTLS_ENABLE, Constants.TRUE);
        properties.put(Constants.MAIL_SMTP_HOST, "host");
        properties.put(Constants.MAIL_SMTP_PORT, "port");
        properties.put(Constants.MAIL_SMTP_SSL_PROTOCOLS, Constants.SMTP_SSL_PROTOCOL);
        return properties;
    }
}