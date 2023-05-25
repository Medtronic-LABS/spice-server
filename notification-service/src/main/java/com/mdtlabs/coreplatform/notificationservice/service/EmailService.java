package com.mdtlabs.coreplatform.notificationservice.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;

/**
 * <p>
 * EmailService is an interface for an email service that defines several methods for sending and managing emails.
 * </p>
 *
 * @author VigneshKumar created on July 22, 2022
 */
public interface EmailService {

    /**
     * <p>
     * This method is used to send email notification using the given email dto.
     * </p>
     *
     * @param email {@link EmailDTO} The notification email that need to be sent is given
     * @return The notification email is created and a boolean value is returned
     */
    boolean sendEmailNotification(EmailDTO email);

    /**
     * <p>
     * This method is used to send email notification using the given email dto.
     * </p>
     *
     * @param emailDto {@link EmailDTO} The notification email that need to be sent is given
     * @return The notification email is sent and a boolean value is returned
     */
    boolean sendEmailUsingStmp(EmailDTO emailDto);

    /**
     * <p>
     * This method used to get an email template based on its type and application type.
     * </p>
     *
     * @param type    {@link String} The type of email template for which the email template is being retrieved is given
     * @param appType {@link String} The type of application for which the email template is being retrieved is given
     * @return {@link EmailTemplate} The email template for the given type and app type is returned
     */
    EmailTemplate getEmailTemplate(String type, String appType);

    /**
     * <p>
     * This method is used to create a notification and sends an email with a specified send status.
     * </p>
     *
     * @param email      {@link EmailDTO} The EmailDTO that contains the details of the email to be sent,
     *                   such as the recipient email address, subject, and message body is given
     * @param sendStatus {@link String} The status of the email sending process is given
     * @return {@link String} The success or failure message is returned
     */
    String createNotificationAndSendEmail(EmailDTO email, String sendStatus);

    /**
     * <p>
     * This method is used to create an outbound email using the provided outbound email details.
     * </p>
     *
     * @param outBoundEmail {@link OutBoundEmail} The necessary information to create an outbound
     *                      email, such as the recipient's email address, the subject,
     *                      and the body of the email is given
     * @return {@link OutBoundEmail} The outbound email is created and returned
     */
    public OutBoundEmail createOutBoundEmail(OutBoundEmail outBoundEmail);

    /**
     * <p>
     * This method is used to update the outbound email using the provided outbound emails.
     * </p>
     *
     * @param outBoundEmails {@link List<OutBoundEmail>} A list of OutBoundEmail objects that need to be
     *                       updated is given
     * @return {@link List<OutBoundEmail>} The list of OutBoundEmails is updated and returned
     */
    public List<OutBoundEmail> updateOutBoundEmails(List<OutBoundEmail> outBoundEmails);

    /**
     * <p>
     * This method is used to get all outbound emails with a specified number of email retry attempts.
     * </p>
     *
     * @return {@link List<OutBoundEmail>} A list of outbound emails is returned
     */
    List<OutBoundEmail> getOutBoundEmails();
}
