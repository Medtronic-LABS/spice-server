package com.mdtlabs.coreplatform.notificationservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.notificationservice.repository.OutBoundSmsRepository;
import com.mdtlabs.coreplatform.notificationservice.service.SmsService;

/**
 * <p>
 * SmsServiceImpl class that implements an interface for saving and retrieving SMS messages using a repository.
 * </p>
 *
 * @author VigneshKumar created on July 22, 2022
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    OutBoundSmsRepository outBoundSmsRepository;

    @Value("${app.sms-retry-attempts}")
    private int smsRetryAttempts;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveOutBoundSms(List<SmsDTO> smsDtoList) {

        List<OutBoundSMS> outBoundSmsList = new ArrayList<>();

        for (SmsDTO smsDto : smsDtoList) {
            OutBoundSMS outBoundSms = new OutBoundSMS();
            if (!Objects.isNull(smsDto.getBody())) {
                outBoundSms.setBody(smsDto.getBody());
            }
            if (!Objects.isNull(smsDto.getToPhoneNo())) {
                outBoundSms.setPhoneNumber(smsDto.getToPhoneNo());
            }
            if (!Objects.isNull(smsDto.getUserName())) {
                outBoundSms.setUserName(smsDto.getUserName());
            }
            if (0 != smsDto.getNotificationId()) {
                outBoundSms.setNotificationId(smsDto.getNotificationId());
            }
            if (0 != smsDto.getTenantId()) {
                outBoundSms.setTenantId(smsDto.getTenantId());
            }
            outBoundSmsList.add(outBoundSms);
        }

        outBoundSmsRepository.saveAll(outBoundSmsList);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public List<OutBoundSMS> getOutBoundSms() {
        return outBoundSmsRepository.getAllSms(smsRetryAttempts);
    }

    /**
     * {@inheritDoc}
     */
    public List<OutBoundSMS> updateOutBoundSms(List<OutBoundSMS> outBoundSms) {
        return outBoundSmsRepository.saveAll(outBoundSms);
    }
}
