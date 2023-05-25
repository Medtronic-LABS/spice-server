package com.mdtlabs.coreplatform.spiceservice;

import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * <p>
 * NotificationApiInterface is used to access notification service APIs. The interface contains methods for saving
 * outbound SMS data, retrieving SMS template values, returning a list of SMS templates, and saving a list of
 * OutBoundSMS
 * </p>
 *
 * @author Niraimathi S
 */
@FeignClient(name = "notification-service")
public interface NotificationApiInterface {

    /**
     * <p>
     * This method is used to save outbound SMS data for a given tenant using authentication token and SMS DTO.
     * </p>
     *
     * @param token    {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                 authenticate the user making the request is given
     * @param tenantId {@link Long}The `tenantId` for whom the SMS messages are being sent or received is given
     * @param smsData  {@link List<SmsDTO>} The smsData is a List of SmsDTO objects that are being sent in the request
     *                 body. These objects contain the data for the outbound SMS messages that need to be saved is given
     * @return {@link String} The method is returning a String
     */
    @PostMapping("/sms/save-outboundsms")
    String saveOutBoundSms(@RequestHeader("Authorization") String token,
                           @RequestHeader("TenantId") Long tenantId, @RequestBody List<SmsDTO> smsData);

    /**
     * <p>
     * This method is used to retrieve SMS template values based on the provided template type, tenant ID,
     * and authorization token.
     * </p>
     *
     * @param token        {@link String} The "Authorization" header is typically used to send a token or credentials to
     *                     authenticate the user making the request is given
     * @param tenantId     {@link Long}The `tenantId` for which the SMS template values are being requested is given
     * @param templateType {@link String} The "templateType" represents the type of SMS template for which the values
     *                     are being requested is given
     * @return {@link ResponseEntity<SMSTemplate>} The ResponseEntity containing an SMSTemplate is being returned
     * with status
     */
    @GetMapping("/sms/get-sms-template-values/{templateType}")
    ResponseEntity<SMSTemplate> getSmsTemplateValues(@RequestHeader("Authorization") String token,
                                                     @RequestHeader("TenantId") Long tenantId, @PathVariable String templateType);

    /**
     * <p>
     * This method is used to return a list of SMS templates based on a list of strings.
     * </p>
     *
     * @param list {@link List<SMSTemplate>} The "list" is used to pass a list of strings to the server-side method
     *             "getSMSTemplates" is given
     * @return {@link List<SMSTemplate>} The list of SMSTemplate is being returned
     */
    @GetMapping("/sms/get-sms-template")
    List<SMSTemplate> getSMSTemplates(@RequestBody List<String> list);

    /**
     * <p>
     * This method is used to save a list of OutBoundSMS objects received in the request body.
     * </p>
     *
     * @param outBoundSMSList {@link List<OutBoundSMS>} The "outBoundSMSList" is a List of "OutBoundSMS" is given
     * @return {@link List<OutBoundSMS>} The list of OutBoundSMS is being returned.
     */
    @PostMapping("/sms/save-outboundsms-values")
    List<OutBoundSMS> saveOutBoundSmsValues(@RequestBody List<OutBoundSMS> outBoundSMSList);
}
