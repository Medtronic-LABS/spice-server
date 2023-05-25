package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * PrescriptionMapperTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PrescriptionMapper class.
 * </p>
 *
 * @author Jaganathan created on Mar 3, 2023
 */
@ExtendWith(MockitoExtension.class)
class PrescriptionMapperTest {

    @InjectMocks
    private PrescriptionMapper prescriptionMapper;

    @Test
    @DisplayName("SetPrescription Test")
    void setPrescription() {
        //given
        Prescription prescription = TestDataProvider.getPrescription();
        prescription.setRemainingPrescriptionDays(2);
        FillPrescriptionRequestDTO requestDTO = TestDataProvider.getPrescriptionResponseDTO();
        //then
        Prescription actualPrescription = prescriptionMapper.setPrescription(prescription, prescription, requestDTO);
        Assertions.assertEquals(prescription, actualPrescription);
    }
}
