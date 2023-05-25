package com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.repository.PatientMedicalComplianceRepository;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service.impl.PatientMedicalComplianceServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientMedicalComplianceServiceImplTest {

    @InjectMocks
    private PatientMedicalComplianceServiceImpl medicalComplianceService;

    @Mock
    private PatientMedicalComplianceRepository medicalComplianceRepository;

    @Test
    @DisplayName("AddMedicalCompliance Test")
    void addMedicalCompliance() {
        List<PatientMedicalCompliance> medicalComplianceList = List.of(TestDataProvider.getPatientMedicalCompliance());
        //when
        when(medicalComplianceRepository.saveAll(medicalComplianceList)).thenReturn(medicalComplianceList);
        //then
        List<PatientMedicalCompliance> actualList = medicalComplianceService
                .addPatientMedicalCompliance(medicalComplianceList);
        Assertions.assertEquals(medicalComplianceList, actualList);
        Assertions.assertEquals(medicalComplianceList.size(), actualList.size());
        Assertions.assertEquals(medicalComplianceList.get(0), actualList.get(0));
    }


    @Test
    @DisplayName("RemoveMedicalCompliance Test")
    void removeMedicalCompliance() {
        List<PatientMedicalCompliance> medicalComplianceList = List.of(TestDataProvider.getPatientMedicalCompliance());
        //when
        when(medicalComplianceRepository.findByPatientTrackId(anyLong())).thenReturn(medicalComplianceList);
        when(medicalComplianceRepository.saveAll(medicalComplianceList)).thenReturn(medicalComplianceList);

        //then
        medicalComplianceService.removeMedicalCompliance(1L);
        Assertions.assertTrue(medicalComplianceList.get(0).isDeleted());
    }

}
