package com.mdtlabs.coreplatform.common.model.dto;

import lombok.Data;

/**
 * <p>
 * This is a Java class representing an email template value with an ID, name, and value.
 * </p>
 */
@Data
public class EmailTemplateValueDTO {

    private long id;

    private String name;

    private String value;

}
