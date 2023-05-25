package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This is DTO class for medical review response
 * </p>
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MedicalReviewResponseDTO {

    private DiagnosisDetailsDTO patientDetails;

    private List<PatientMedicalReviewDTO> medicalReviews;

    private List<Map<String, Object>> patientReviewDates;

    private List<PatientMedicalReviewDTO> patientMedicalReview;

    private List<PrescriptionResponseDTO> prescriptions;

    private List<PatientLabTest> investigations;

    private ReviewerDetailsDTO reviewerDetails;

    private Boolean isSigned;

    private Date reviewedAt;

    private String medicalReviewFrequency;

    /**
     * <p>
     * The constructor assigns the values of the parameters to the corresponding
     * instance variables of the class.
     * </p>
     *
     * @param patientDetails         {@link DiagnosisDetailsDTO} `patientDetails` of type `DiagnosisDetailsDTO`
     * @param medicalReviews         {@link List<PatientMedicalReviewDTO>} `medicalReviews` of type `List<PatientMedicalReviewDTO>`
     * @param patientReviewDates     {@link List<Map<String, Object>>} `patientReviewDates` of type `List<Map<String, Object>>`
     * @param patientMedicalReview   {@link List<PatientMedicalReviewDTO>} `patientMedicalReview` of type `List<PatientMedicalReviewDTO>`
     * @param prescriptions          {@link List<PrescriptionResponseDTO>} `prescriptions` of type `List<PrescriptionResponseDTO>`
     * @param investigations         {@link List<PatientLabTest>} `investigations` of type `List<PatientLabTest>`
     * @param reviewerDetails        {@link ReviewerDetailsDTO} `reviewerDetails` of type `ReviewerDetailsDTO`
     * @param isSigned               `isSigned` of type `Boolean`
     * @param reviewedAt             `reviewedAt` of type `Date`
     * @param medicalReviewFrequency `medicalReviewFrequency` of type `String`
     */
    public MedicalReviewResponseDTO(DiagnosisDetailsDTO patientDetails, List<PatientMedicalReviewDTO> medicalReviews,
                                    List<Map<String, Object>> patientReviewDates, List<PatientMedicalReviewDTO> patientMedicalReview,
                                    List<PrescriptionResponseDTO> prescriptions, List<PatientLabTest> investigations, ReviewerDetailsDTO reviewerDetails,
                                    Boolean isSigned, Date reviewedAt, String medicalReviewFrequency) {
        this.patientDetails = patientDetails;
        this.medicalReviews = medicalReviews;
        this.patientReviewDates = patientReviewDates;
        this.patientMedicalReview = patientMedicalReview;
        this.prescriptions = prescriptions;
        this.investigations = investigations;
        this.reviewerDetails = reviewerDetails;
        this.isSigned = isSigned;
        this.reviewedAt = reviewedAt;
        this.medicalReviewFrequency = medicalReviewFrequency;
    }

    public MedicalReviewResponseDTO() {
    }

    /**
     * <p>
     * This constructor can be used to create a new instance of `MedicalReviewResponseDTO` with
     * only the `patientReviewDates` and `patientMedicalReview` fields initialized.
     * </p>
     * 
     * @param patientReviewDates {@link List<Map<String, Object>>} param is passed to MedicalReviewResponseDTO
     * @param patientMedicalReview {@link List<PatientMedicalReviewDTO>} param is passed to MedicalReviewResponseDTO
     */
    public MedicalReviewResponseDTO(List<Map<String, Object>> patientReviewDates, List<PatientMedicalReviewDTO> patientMedicalReview) {
        this.patientReviewDates = patientReviewDates;
        this.patientMedicalReview = patientMedicalReview;
    }


}
