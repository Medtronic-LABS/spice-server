package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import org.springframework.stereotype.Component;

/**
 * This is the mapper class to map the entity and POJO class.
 *
 * @author Jeyaharini T A created on Feb 06, 2023
 */
@Component
public class PatientMedicalReviewMapper {

    /**
     * <p>
     * The function sets values from a MedicalReviewDTO object to a PatientMedicalReview object.
     * </p>
     *
     * @param medicalReviewDto {@link MedicalReviewDTO} an object of type MedicalReviewDTO that contains information about a
     *                         patient's medical review, including the tenant ID, patient track ID, patient visit ID, clinical
     *                         note, physical exam comments, and compliant comments.
     * @return {@link PatientMedicalReview} A PatientMedicalReview object is being returned.
     */
    public PatientMedicalReview setMedicalReviewDto(MedicalReviewDTO medicalReviewDto) {
        PatientMedicalReview patientMedicalReview = new PatientMedicalReview();
        patientMedicalReview.setTenantId(medicalReviewDto.getTenantId());
        patientMedicalReview.setPatientTrackId(medicalReviewDto.getPatientTrackId());
        patientMedicalReview.setPatientVisitId(medicalReviewDto.getPatientVisitId());
        patientMedicalReview.setClinicalNote(medicalReviewDto.getContinuousMedicalReview().getClinicalNote());
        patientMedicalReview
                .setPhysicalExamComments(medicalReviewDto.getContinuousMedicalReview().getPhysicalExamComments());
        patientMedicalReview.setCompliantComments(medicalReviewDto.getContinuousMedicalReview().getComplaintComments());
        return patientMedicalReview;
    }

}
