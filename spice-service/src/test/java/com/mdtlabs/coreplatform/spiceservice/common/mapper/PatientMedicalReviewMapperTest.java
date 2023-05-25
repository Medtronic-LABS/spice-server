package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * PatientMedicalReviewMapperTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientMedicalReviewMapper class.
 * </p>
 *
 * @author Jaganathan created on Mar 2, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientMedicalReviewMapperTest {

    @InjectMocks
    private PatientMedicalReviewMapper medicalReviewMapper;

    @Test
    @DisplayName("SetMedicalReviewDTO Test")
    void setMedicalReviewDTO() {
        //given
        PatientMedicalReview patientMedicalReview = TestDataProvider.getPatientMedicalReview();
        MedicalReviewDTO medicalReviewDTO = TestDataProvider.getMedicalReviewDTO();
        //then
        PatientMedicalReview actualPatientMedicalReview = medicalReviewMapper.setMedicalReviewDto(medicalReviewDTO);
        Assertions.assertEquals(patientMedicalReview, actualPatientMedicalReview);
    }
}
