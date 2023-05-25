package com.mdtlabs.coreplatform.notificationservice.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;

/**
 * <p>
 * SmsService is an interface for a service that deals with OutBoundSMS.
 * </p>
 *
 * @author Jeyaharini T A
 */
public interface SmsService {

    /**
     * <p>
     * This method is used to save outbound sms using the given list of sms DTOs.
     * </p>
     *
     * @param smsDataList {@link List<SmsDTO>} The list of sms DTO that need to be created is given
     * @return Returns a boolean value after the outbound sms is created
     */
    boolean saveOutBoundSms(List<SmsDTO> smsDataList);

    /**
     * <p>
     * This method is used to get all outbound SMS messages.
     * </p>
     *
     * @return {@link List<OutBoundSMS>} A list of OutBoundSMS is returned
     */
    public List<OutBoundSMS> getOutBoundSms();

    /**
     * <p>
     * This method is used to save and to update a list of OutBoundSMSs.
     * </p>
     *
     * @param outBoundSms {@link List<OutBoundSMS>} The list of OutBoundSMSs that need to be updated is given
     * @return {@link List<OutBoundSMS>} The list of OutBoundSMSs is returned after the outboundSmsList is updated
     */
    public List<OutBoundSMS> updateOutBoundSms(List<OutBoundSMS> outBoundSms);
}
