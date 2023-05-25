package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * This is a Java class representing a data transfer object for compliance information, with fields for
 * ID, name, compliance ID, existence of child, and other compliance.
 * </p>
 */
@Data
@Validated
public class ComplianceDTO {

    private Long id;

    @NotNull(message = ErrorConstants.COMPLIANCE_NAME_NOT_NULL)
    private String name;

    @NotNull(message = ErrorConstants.COMPLIANCE_ID_NOT_NULL)
    private Long complianceId;

    private Boolean isChildExist;

    private String otherCompliance;
}
