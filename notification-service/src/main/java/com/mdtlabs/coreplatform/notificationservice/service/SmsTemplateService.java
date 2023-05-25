package com.mdtlabs.coreplatform.notificationservice.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;

/**
 * <p>
 * SmsTemplateService is an interface for a service that deals with SMS templates.
 * </p>
 *
 * @author Jeyaharini T A
 */
public interface SmsTemplateService {

    /**
     * <p>
     * This method is used to get an SMSTemplate by its type.
     * </p>
     *
     * @param templateType {@link String} The type of SMS template that needs to be found is given
     * @return {@link SMSTemplate} The SMSTemplate for the given template type is returned
     */
    SMSTemplate getSmsTemplateValues(String templateType);

    /**
     * <p>
     * This method is used to get a list of SMS templates based on a list of given SMSTemplate types.
     * </p>
     *
     * @param list {@link List<String>} The list of SMS template types that need to be found is given
     * @return {@link List<SMSTemplate>} A list of SMSTemplate for the given list of SMS template
     * types is returned
     */
    List<SMSTemplate> getSMSTemplates(List<String> list);

}
