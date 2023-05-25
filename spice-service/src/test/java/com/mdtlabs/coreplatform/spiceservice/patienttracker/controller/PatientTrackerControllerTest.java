package com.mdtlabs.coreplatform.spiceservice.patienttracker.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.ConfirmDiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientTrackerControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientTrackerController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientTrackerControllerTest {

    @InjectMocks
    private PatientTrackerController patientTrackerController;
    @Mock
    private PatientTrackerService patientTrackerService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(PatientTrackerController.class, "modelMapper", patientTrackerController);
    }
    private PatientTracker patientTracker = TestDataProvider.getPatientTracker();

    private ConfirmDiagnosisDTO diagnosisDTO = TestDataProvider.getConfirmDiagnosisDTO();
    private PatientTracker actualPatientTracker;

    private SuccessResponse<PatientTracker> response;

    private SuccessResponse<ConfirmDiagnosisDTO> diagnosisResponse;

    @Test
    @DisplayName("AddPatientTracker Test")
    void addPatientTracker() {
        //given
        PatientTrackerDTO patientTrackerDTO = TestDataProvider.getPatientTrackerDTO();
        //when
        when(modelMapper.map(patientTrackerDTO, PatientTracker.class)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        //then
        response = patientTrackerController.addPatientTracker(patientTrackerDTO);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(patientTracker);
        actualPatientTracker = patientTrackerService.addOrUpdatePatientTracker(patientTracker);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
        Assertions.assertEquals(patientTracker.getPatientId(), actualPatientTracker.getPatientId());
        Assertions.assertNotEquals(patientTracker,
                patientTrackerService.addOrUpdatePatientTracker(new PatientTracker()));

    }

    @Test
    @DisplayName("UpdateConfirmDiagnosis Test")
    void updateConfirmDiagnosis() {
        //when
        when(patientTrackerService.updateConfirmDiagnosis(diagnosisDTO)).thenReturn(diagnosisDTO);
        //then
        diagnosisResponse = patientTrackerController.updateConfirmDiagnosis(diagnosisDTO);
        Assertions.assertEquals(HttpStatus.OK, diagnosisResponse.getStatusCode());
        Assertions.assertEquals(diagnosisDTO, patientTrackerService.updateConfirmDiagnosis(diagnosisDTO));
        Assertions.assertEquals(diagnosisDTO.getIsConfirmDiagnosis(),
                patientTrackerService.updateConfirmDiagnosis(diagnosisDTO).getIsConfirmDiagnosis());
    }
}
