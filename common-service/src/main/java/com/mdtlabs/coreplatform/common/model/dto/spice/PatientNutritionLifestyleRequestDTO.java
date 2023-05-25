package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * This is a Java class representing a request for patient nutrition and lifestyle information,
 * including various fields such as lifestyle assessment, referral information, and clinician notes.
 * </p>
 */
@Data
public class PatientNutritionLifestyleRequestDTO {

    private Set<Long> lifestyle;

    private List<PatientNutritionLifestyle> lifestyles;

    private String lifestyleAssessment;

    private boolean isNutritionist;

    private Long referredBy;

    private Date referredDate;

    private String referredByDisplay;

    private String referredFor;

    private Long patientTrackId;

    private Long tenantId;

    private Long patientVisitId;

    private String clinicianNote;

    private String otherNote;

    private boolean isNutritionHistoryRequired = false;

    private String roleName;
}
