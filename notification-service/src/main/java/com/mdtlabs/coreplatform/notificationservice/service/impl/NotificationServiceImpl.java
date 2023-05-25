package com.mdtlabs.coreplatform.notificationservice.service.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.Notification;
import com.mdtlabs.coreplatform.notificationservice.repository.NotificationRepository;
import com.mdtlabs.coreplatform.notificationservice.service.NotificationService;

/**
 * <p>
 * NotificationServiceImpl class implements a NotificationService interface and provides methods for saving, updating,
 * deleting, and retrieving notifications, as well as creating and sending email notifications using a thread pool.
 * </p>
 *
 * @author VigneshKumar created on July 22, 2022
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private ExecutorService executor;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * <p>
     * This constructor is used to create new thread from thread pool for executor object.
     * </p>
     */
    public NotificationServiceImpl() {
        executor = Executors.newCachedThreadPool();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> getAllNotification() {
        return notificationRepository.getAllNotification();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteNotificationById(long notificationId) {
        return notificationRepository.updateNotificationStatusById(notificationId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification getNotificationById(long notificationId) {
        return notificationRepository.getNotificationById(notificationId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createNotificationAndSendEmail(EmailDTO email, String sendStatus) {
        try {
            Notification notification = new Notification(email.getSubject(),
                    email.getBody(), email.getToMails());
            notification.setStatus(Constants.ERROR.equalsIgnoreCase(sendStatus)
                    ? Constants.NOTIFICATION_STATUS_FAILED : Constants.NOTIFICATION_STATUS_PROCESSED);
            if (null != saveNotification(notification)) {
                return Constants.SUCCESS;
            } else {
                Logger.logInfo(Constants.SAVING_EMAIL_NOTIFICATION_ERROR);
                return Constants.ERROR;
            }
        } catch (Exception error) {
            Logger.logError(error);
            return Constants.ERROR;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeRunnable(Runnable runnable) {
        executor.execute(runnable);
    }

}