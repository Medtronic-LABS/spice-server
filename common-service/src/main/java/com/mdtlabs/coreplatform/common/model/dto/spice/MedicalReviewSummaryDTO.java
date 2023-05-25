package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * The MedicalReviewSummaryDTO class is a Java class that represents a summary of a patient's medical
 * review, including medical reviews, prescriptions, lab tests, reviewer details, and review frequency.
 * </P>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalReviewSummaryDTO {

    private List<PatientMedicalReviewDTO> medicalReviews;

    private List<PatientVisit> patientReviewDates;

    private List<Object> prescriptions;

    private List<PatientLabTest> investigations;

    private ReviewerDetailsDTO reviewerDetails;

    private boolean isSigned;

    private Date reviewedAt;

    private String medicalReviewFrequency;

    /**
     * <p>
     * The constructor assigns the values of these
     * parameters to the corresponding instance variables of the class using the `this` keyword.
     * </p>
     *
     * @param medicalReviews         {@link List<PatientMedicalReviewDTO>} param is passed in MedicalReviewSummaryDTO
     * @param prescriptions          {@link List<Object>} param is passed in MedicalReviewSummaryDTO
     * @param investigations         {@link List<PatientLabTest>} param is passed in MedicalReviewSummaryDTO
     * @param reviewerDetails        {@link ReviewerDetailsDTO} param is passed in MedicalReviewSummaryDTO
     * @param isSigned               param is passed in MedicalReviewSummaryDTO
     * @param reviewedAt             param is passed in MedicalReviewSummaryDTO
     * @param medicalReviewFrequency param is passed in MedicalReviewSummaryDTO
     */
    public MedicalReviewSummaryDTO(List<PatientMedicalReviewDTO> medicalReviews,
                                   List<Object> prescriptions, List<PatientLabTest> investigations, ReviewerDetailsDTO reviewerDetails,
                                   boolean isSigned, Date reviewedAt, String medicalReviewFrequency) {
        this.medicalReviews = medicalReviews;
        this.prescriptions = prescriptions;
        this.investigations = investigations;
        this.reviewerDetails = reviewerDetails;
        this.isSigned = isSigned;
        this.reviewedAt = reviewedAt;
        this.medicalReviewFrequency = medicalReviewFrequency;
    }

    public MedicalReviewSummaryDTO() {
    }

}
