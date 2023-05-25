package com.mdtlabs.coreplatform.notificationservice.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.Notification;

/**
 * <p>
 * This is an interface for a NotificationService in a Java program. It defines several methods for creating,
 * retrieving, updating, and deleting notifications, as well as sending emails and creating
 * new threads for the notification service.
 * </p>
 *
 * @author VigneshKumar created on July 22, 2022
 */
public interface NotificationService {

    /**
     * <p>
     * This method is used to create a notification using given Notification details.
     * </p>
     *
     * @param notification {@link Notification} The updated information for a notification that needs to
     *                     be created is given
     * @return {@link Notification} The notification is created for the given notification details and returned
     */
    Notification saveNotification(Notification notification);

    /**
     * <p>
     * This method is used to retrieve all notifications from a database.
     * </p>
     *
     * @return {@link List<Notification>} A list of notifications is returned
     */
    List<Notification> getAllNotification();

    /**
     * <p>
     * This method is used to update a notification using given Notification details.
     * </p>
     *
     * @param notification {@link Notification} The updated information for a notification that needs to
     *                     be updated is given
     * @return {@link Notification} The notification is updated for the given notification details and returned
     */
    Notification updateNotification(Notification notification);

    /**
     * <p>
     * This method used to delete the notification detail for the given ID.
     * </p>
     *
     * @param notificationId The notification ID to delete a specific notification is given
     * @return The notification for the given notification ID is deleted and
     * the int value is returned
     */
    int deleteNotificationById(long notificationId);

    /**
     * <p>
     * This method used to get the notification detail for the given ID.
     * </p>
     *
     * @param notificationId The notification ID to retrieve a specific notification is given
     * @return {@link Notification} The notification for the given notification ID is returned
     */
    Notification getNotificationById(long notificationId);

    /**
     * <p>
     * This method is used to create a notification and sends an email with a specified send status.
     * </p>
     *
     * @param email      {@link EmailDTO} The EmailDTO that contains the details of the email to be sent, such as the recipient
     *                   email address, subject, and message body is given
     * @param sendStatus {@link String} The status of the notification to be sent is given
     * @return {@link String} The success or failure message is returned
     */
    String createNotificationAndSendEmail(EmailDTO email, String sendStatus);

    /**
     * <p>
     * This method is used to create new runnable thread for notification service.
     * </p>
     *
     * @param runnable {@link Runnable} A parameter that implements the Runnable interface is given
     */
    void executeRunnable(Runnable runnable);
}