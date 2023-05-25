package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This is DTO class for patient nutrition entity
 * </p>
 */
@Data
public class NutritionLifestyleResponseDTO {

    private Long id;

    private List<Map<String, String>> lifestyle;

    private ReviewerDetailsDTO referredBy;

    private ReviewerDetailsDTO assessedBy;

    private Date referredDate;

    private Date assessedDate;

    private String clinicianNote;

    private String lifestyleAssessment;

    private String otherNote;

}
