package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This class contains the fields to sort the patients
 * </p>
 *
 * @author Jeyaharini T A
 */
@Data
public class PatientSortDTO {

    private Boolean isRedRisk;

    private Boolean isLatestAssessment;

    private Boolean isMedicalReviewDueDate;

    private Boolean isHighLowBp;

    private Boolean isHighLowBg;

    private Boolean isAssessmentDueDate;

    private Boolean isUpdated;

    private Boolean isCvdRisk;

}
