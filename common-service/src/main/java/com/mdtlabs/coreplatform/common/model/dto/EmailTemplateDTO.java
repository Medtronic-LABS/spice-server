package com.mdtlabs.coreplatform.common.model.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * The EmailTemplateDTO class is a data transfer object that contains information about an email
 * template, including its ID, type, content, title, URL, and a list of values.
 * </p>
 */
@Data
public class EmailTemplateDTO {

    private long id;

    private String type;

    private String vmContent;

    private String body;

    private String title;

    private String appUrl;

    private List<EmailTemplateValueDTO> emailTemplateValues;


}
