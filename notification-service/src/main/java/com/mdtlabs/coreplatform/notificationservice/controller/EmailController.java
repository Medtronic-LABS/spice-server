package com.mdtlabs.coreplatform.notificationservice.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OutBoundEmailDTO;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessCode;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.notificationservice.service.EmailService;

/**
 * <p>
 * The EmailController class contains methods for sending email notifications, retrieving
 * email templates, creating outbound emails, and performing a health check.
 * </p>
 *
 * @author VigneshKumar created on 22 July 2022
 */
@RestController
@RequestMapping(value = "/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to send email notification using the given email dto.
     * </p>
     *
     * @param emailDto {@link EmailDTO} The notification email that need to be sent is given
     * @return {@link SuccessResponse<Boolean>} The notification email is sent and returned with the status
     */
    @PostMapping(value = "/send-email")
    public SuccessResponse<Boolean> sendEmailNotification(@RequestBody EmailDTO emailDto) {
        return new SuccessResponse(SuccessCode.SEND_EMAIL_USING_SMTP,
                emailService.sendEmailNotification(emailDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method used to get an email template based on its type and application type.
     * </p>
     *
     * @param type    {@link String} The type of email template for which the email template is being retrieved is given
     * @param appType {@link String} The type of application for which the email template is being retrieved is given
     * @return {@link ResponseEntity<EmailTemplate>} The email template for the given type and app type is
     * returned with the status
     */
    @GetMapping(value = "/email-type/{type}/{appType}")
    public ResponseEntity<EmailTemplate> getEmailTemplate(@PathVariable(Constants.TYPE) String type,
                                                          @PathVariable(Constants.APP_TYPE) String appType) {
        return ResponseEntity.ok().body(emailService.getEmailTemplate(type, appType));
    }

    /**
     * <p>
     * This method is used to create an outbound email using the given outbound email dto.
     * </p>
     *
     * @param emailDto {@link OutBoundEmailDTO} The outbound email that need to be created is given
     * @return {@link ResponseEntity<Boolean>} The created outbound email is returned with the status
     */
    @PostMapping(value = "/create")
    public ResponseEntity<Boolean> createOutBoundEmail(@RequestBody OutBoundEmailDTO emailDto) {
        OutBoundEmail outBoundEmail = emailService.createOutBoundEmail(modelMapper.map(emailDto, OutBoundEmail.class));
        return ResponseEntity.ok().body(null != outBoundEmail);
    }

    /**
     * <p>
     * This method is used to return a success response with a boolean value indicating the
     * health status of the application.
     * </p>
     *
     * @return {@link SuccessResponse<Boolean>} Returns a boolean value of TRUE and status indicating that
     * the health check was successful
     */
    @GetMapping("/health-check")
    public SuccessResponse<Boolean> healthCheck() {
        return new SuccessResponse(SuccessCode.HEALTH_CHECK,
                Boolean.TRUE, HttpStatus.OK);
    }
}
