package com.mdtlabs.coreplatform.notificationservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessCode;
import com.mdtlabs.coreplatform.notificationservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.notificationservice.service.SmsService;
import com.mdtlabs.coreplatform.notificationservice.service.SmsTemplateService;

/**
 * <p>
 * SMS Controller class that defines REST API endpoints for managing SMS templates and outbound SMS messages.
 * </p>
 *
 * @author Vigneshkumar created on 22 July 2022
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    SmsService smsService;

    @Autowired
    SmsTemplateService templateService;

    @Autowired
    private SmsTemplateService smsTemplateService;

    /**
     * <p>
     * This method is used to save outbound sms using the given list of sms DTOs.
     * </p>
     *
     * @param smsDataList {@link List<SmsDTO>} The list of sms DTO that need to be created is given
     * @return {@link SuccessResponse<String>} Returns a success message and status after the
     * outbound sms is created
     */
    @PostMapping("/save-outboundsms")
    public SuccessResponse<String> saveOutBoundSms(@RequestBody List<SmsDTO> smsDataList) {
        smsService.saveOutBoundSms(smsDataList);
        return new SuccessResponse<>(SuccessCode.NOTIFICATION_SAVE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get an SMSTemplate by its type.
     * </p>
     *
     * @param templateType {@link String} The type of SMS template that needs to be found is given
     * @return {@link ResponseEntity<SMSTemplate>} The SMSTemplate for the given template type is returned with status
     */
    @GetMapping("/get-sms-template-values/{templateType}")
    public ResponseEntity<SMSTemplate> getSmsTemplateValues(@PathVariable("templateType") String templateType) {
        return ResponseEntity.ok().body(templateService.getSmsTemplateValues(templateType));
    }

    /**
     * <p>
     * This method is used to get a list of SMS templates based on a list of given SMSTemplate types.
     * </p>
     *
     * @param list {@link List<String>} The list of SMS template types that need to be found is given
     * @return {@link List<SMSTemplate>} A list of SMSTemplate for the given list of SMS template
     * types is returned
     */
    @GetMapping("/get-sms-template")
    public List<SMSTemplate> getSMSTemplates(@RequestBody List<String> list) {
        return smsTemplateService.getSMSTemplates(list);
    }


    /**
     * <p>
     * This method is used to save and to update a list of OutBoundSMSs.
     * </p>
     *
     * @param outBoundSmsList {@link List<OutBoundSMS>} The list of OutBoundSMSs that need to be updated is given
     * @return {@link List<OutBoundSMS>} The list of OutBoundSMSs is returned after the outboundSmsList is updated
     */
    @PostMapping("/save-outboundsms-values")
    public List<OutBoundSMS> saveOutBoundSmsValues(@RequestBody List<OutBoundSMS> outBoundSmsList) {
        return smsService.updateOutBoundSms(outBoundSmsList);
    }
}
