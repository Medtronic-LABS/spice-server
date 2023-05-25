package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This DTO class is used for common Get request.
 * </p>
 *
 * @author Karthick M created Feb 07, 2023
 */
@Data
public class GetRequestDTO extends RequestDTO {

    private boolean isDeleted;

    private boolean isLatestRequired;

    private Long prescriptionId;

    private String roleName;

    private String comment;

    private Long patientPregnancyId;
}
