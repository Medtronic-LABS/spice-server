package com.mdtlabs.coreplatform.notificationservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.notificationservice.repository.SmsTemplateRepository;
import com.mdtlabs.coreplatform.notificationservice.service.SmsTemplateService;

/**
 * <p>
 * SmsTemplateServiceImpl class implements the SmsTemplateService interface and provides methods to retrieve SMS templates
 * from a repository.
 * </p>
 *
 * @author VigneshKumar created on July 22, 2022
 */
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {

    @Autowired
    SmsTemplateRepository smsTemplateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SMSTemplate getSmsTemplateValues(String templateType) {
        return smsTemplateRepository.findByType(templateType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SMSTemplate> getSMSTemplates(List<String> list) {
        return smsTemplateRepository.getSMSTemplates(Constants.TEMPLATE_LIST);
    }
}
