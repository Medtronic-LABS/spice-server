package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is a Java class called ComplicationDTO that has two private fields, complicationId of type Long
 * and otherComplication of type String, and uses the Lombok @Data annotation.
 * </p>
 */
@Data
public class ComplicationDTO {

    private Long complicationId;

    private String otherComplication;
}
