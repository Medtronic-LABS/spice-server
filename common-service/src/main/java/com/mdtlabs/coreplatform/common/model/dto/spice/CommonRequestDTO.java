package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * This DTO class handles the common requests
 * </p>
 *
 * @author Karthick M created on Feb 07, 2023
 */
@Data
public class CommonRequestDTO {

    private Long id;

    private Long patientTrackId;

    private Long countryId;

    private List<String> roleNames;

    private String searchTerm;

    private Long patientVisitId;

    private Long tenantId;

    public CommonRequestDTO(Long patientTrackId) {
        this.patientTrackId = patientTrackId;
    }

    public CommonRequestDTO() {
    }
}
