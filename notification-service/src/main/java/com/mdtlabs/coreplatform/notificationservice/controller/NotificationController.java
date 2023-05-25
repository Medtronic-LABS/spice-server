package com.mdtlabs.coreplatform.notificationservice.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.NotificationDTO;
import com.mdtlabs.coreplatform.common.model.entity.Notification;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessCode;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.notificationservice.service.NotificationService;

/**
 * <p>
 * Notification Controller defines REST API endpoints for creating, retrieving, updating, and deleting notifications
 * using a NotificationDTO object.
 * </p>
 *
 * @author Vigneshkumar created on 22 July 2022
 */
@RestController
@RequestMapping(value = "/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create a notification using a NotificationDTO object.
     * </p>
     *
     * @param notificationDto {@link NotificationDTO} The updated information for a notification that needs to
     *                        be created is given
     * @return {@link SuccessResponse<Notification>} Returns a success message and status with the created Notification
     */
    @PostMapping
    public SuccessResponse<Notification> saveNotification(@RequestBody NotificationDTO notificationDto) {
        return new SuccessResponse<>(SuccessCode.NOTIFICATION_SAVE,
                notificationService.saveNotification(modelMapper.map(notificationDto, Notification.class)),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve all notifications from a database.
     * </p>
     *
     * @return {@link List<Notification>} A list of notifications is returned
     */
    @GetMapping
    public SuccessResponse<Notification> getAllNotification() {
        return new SuccessResponse<>(SuccessCode.GET_NOTIFICATIONS, notificationService.getAllNotification(),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update a notification using a NotificationDTO object.
     * </p>
     *
     * @param notificationDto {@link NotificationDTO} The updated information for a notification that needs to
     *                        be updated is given
     * @return {@link SuccessResponse<Notification>} Returns a status with the updated Notification
     */
    @PutMapping
    public SuccessResponse<Notification> updateNotification(@RequestBody NotificationDTO notificationDto) {
        return new SuccessResponse<>(SuccessCode.NOTIFICATION_UPDATE,
                notificationService.updateNotification(modelMapper.map(notificationDto, Notification.class)),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method used to delete the notification detail for the given ID.
     * </p>
     *
     * @param notificationId The notification ID to delete a specific notification is given
     * @return {@link SuccessResponse<Notification>} The notification for the given notification ID is deleted and
     * the success message with status is returned
     */
    @PostMapping(value = "/{notificationId}")
    public SuccessResponse<Notification> deleteNotificationById(
            @PathVariable(value = Constants.NOTIFICATION_ID) long notificationId) {
        return new SuccessResponse<>(SuccessCode.NOTIFICATION_DELETE,
                notificationService.deleteNotificationById(notificationId), HttpStatus.OK);
    }

    /**
     * <p>
     * This method used to get the notification detail for the given ID.
     * </p>
     *
     * @param notificationId The notification ID to retrieve a specific notification is given
     * @return {@link SuccessResponse<Notification>} The notification for the given notification ID is returned
     * with the success message and status
     */
    @GetMapping(value = "/{notificationId}")
    public SuccessResponse<Notification> getNotificationById(
            @PathVariable(value = Constants.NOTIFICATION_ID) long notificationId) {
        return new SuccessResponse<>(SuccessCode.GET_NOTIFICATION,
                notificationService.getNotificationById(notificationId), HttpStatus.OK);
    }
}