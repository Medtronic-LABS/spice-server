package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * This is DTO class for patient labtest entity
 * </p>
 */
@Data
public class PatientLabTestDTO {

    private Long id;

    private Long labTestId;

    private String labTestName;

    private ReviewerDetailsDTO referredBy;

    private Date referredDate;

    private Long patientVisitId;

    private ReviewerDetailsDTO resultUpdateBy;

    private Boolean isAbnormal;

    private Boolean isReviewed;

    private Date resultDate;

    private String resultComments;

}