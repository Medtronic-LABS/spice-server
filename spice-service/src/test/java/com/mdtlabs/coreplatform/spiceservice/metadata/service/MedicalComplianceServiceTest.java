package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.MedicalComplianceRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.MedicalComplianceServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Medical Compliance Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MedicalComplianceServiceTest {

    @InjectMocks
    private MedicalComplianceServiceImpl medicalComplianceService;

    @Mock
    private MedicalComplianceRepository medicalComplianceRepository;

    @Test
    void getMedicalCompliances() {
        //given
        List<MedicalCompliance> medicalComplianceList = List.of(TestDataProvider.getMedicalCompliance());

        //when
        when(medicalComplianceRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(medicalComplianceList);

        //then
        List<MedicalCompliance> medicalCompliances = medicalComplianceService.getMedicalComplianceList();
        Assertions.assertEquals(medicalComplianceList.size(), medicalCompliances.size());
    }

    @Test
    void testGetMedicalCompliances() {
        //given
        List<MedicalCompliance> medicalComplianceList = List.of(TestDataProvider.getMedicalCompliance());

        //when
        when(medicalComplianceRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(medicalComplianceList);

        //then
        List<MedicalCompliance> medicalCompliances = medicalComplianceService.getMedicalComplianceList();
        Assertions.assertEquals(medicalComplianceList.get(0), medicalCompliances.get(0));
    }
}