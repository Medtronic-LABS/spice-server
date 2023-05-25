package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The PatientLifestyleDTO class contains fields for smoking status, alcohol status, nutrition, and
 * physical activity.
 * </p>
 */
@Data
public class PatientLifestyleDTO {

    private String smokingStatus;

    private String alcoholStatus;

    private String nutrition;

    private String physicalActivity;

}
