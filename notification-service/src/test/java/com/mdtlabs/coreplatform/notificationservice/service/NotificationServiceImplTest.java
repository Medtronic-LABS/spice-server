package com.mdtlabs.coreplatform.notificationservice.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.Notification;
import com.mdtlabs.coreplatform.notificationservice.repository.NotificationRepository;
import com.mdtlabs.coreplatform.notificationservice.service.impl.NotificationServiceImpl;
import com.mdtlabs.coreplatform.notificationservice.util.TestDataProvider;

/**
 * <p>
 * Notification Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private ExecutorService executor;

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    void saveNotification() {
        //given
        Notification notification = TestDataProvider.getNotification();
        notification.setId(1L);

        //when
        when(notificationRepository.save(notification)).thenReturn(notification);

        //then
        Notification result = notificationService.saveNotification(notification);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void getAllNotification() {
        //given
        Notification notification = TestDataProvider.getNotification();
        notification.setId(1L);
        List<Notification> notificationList = List.of(notification);

        //when
        when(notificationRepository.getAllNotification()).thenReturn(notificationList);

        //then
        List<Notification> result = notificationService.getAllNotification();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.get(0).getId());
    }

    @Test
    void updateNotification() {
        //given
        Notification notification = TestDataProvider.getNotification();
        notification.setId(1L);

        //when
        when(notificationRepository.save(notification)).thenReturn(notification);

        //then
        Notification result = notificationService.updateNotification(notification);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void deleteNotificationById() {
        //given
        Notification notification = TestDataProvider.getNotification();
        notification.setId(1L);

        //when
        when(notificationRepository.updateNotificationStatusById(1L)).thenReturn(1);

        //then
        int result = notificationService.deleteNotificationById(1L);
        Assertions.assertEquals(1, result);
    }

    @Test
    void getNotificationById() {
        //given
        Notification notification = TestDataProvider.getNotification();
        notification.setId(1L);

        //when
        when(notificationRepository.getNotificationById(1L)).thenReturn(notification);

        //then
        Notification result = notificationService.getNotificationById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void createNotificationAndSendEmail() {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        emailDTO.setSubject("testSubject");
        emailDTO.setBody("testBody");
        emailDTO.setToMails("testToMails");
        Notification notification = new Notification(emailDTO.getSubject(),
                emailDTO.getBody(), emailDTO.getToMails());
        notification.setStatus(Constants.NOTIFICATION_STATUS_PROCESSED);

        //when
        when(notificationRepository.save(notification)).thenReturn(notification);

        //then
        String result = notificationService.createNotificationAndSendEmail(emailDTO, Constants.NOTIFICATION);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Constants.SUCCESS, result);
    }

    @Test
    void toVerifyCreateNotificationAndSendEmail() {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        emailDTO.setSubject("testSubject");
        emailDTO.setBody("testBody");
        emailDTO.setToMails("testToMails");
        Notification notification = new Notification(emailDTO.getSubject(),
                emailDTO.getBody(), emailDTO.getToMails());

        //when
        when(notificationRepository.save(notification)).thenReturn(notification);

        //then
        String result = notificationService.createNotificationAndSendEmail(emailDTO, Constants.ERROR);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Constants.ERROR, result);
    }

    @Test
    void createNotificationAndSendEmailWithNull() {
        //given
        EmailDTO emailDTO = TestDataProvider.getEmailDTO();
        emailDTO.setSubject("");
        emailDTO.setBody("");
        emailDTO.setToMails("");
        Notification notification = new Notification();
        notification.setId(1L);

        //when
        when(notificationRepository.save(notification)).thenReturn(notification);

        //then
        String result = notificationService.createNotificationAndSendEmail(emailDTO, "");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Constants.ERROR, result);
    }

    @Test
    void toVerifyCreateNotificationAndSendEmailWithException() {
        //then
        String result = notificationService.createNotificationAndSendEmail(null, "");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Constants.ERROR, result);
    }

    @Test
    void executeRunnable() {
        //given
        Runnable runnable = mock(Runnable.class);

        //when
        doNothing().when(executor).execute(runnable);

        //then
        notificationService.executeRunnable(runnable);
        verify(executor, atLeastOnce()).execute(runnable);
    }
}