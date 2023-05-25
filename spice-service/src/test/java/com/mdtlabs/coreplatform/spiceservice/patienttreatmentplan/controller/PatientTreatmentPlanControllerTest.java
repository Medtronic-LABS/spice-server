package com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTreatmentPlanDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientTreatmentPlanControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientTreatmentPlanController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientTreatmentPlanControllerTest {

    @Mock
    private PatientTreatmentPlanService treatmentPlanService;

    @InjectMocks
    private PatientTreatmentPlanController treatmentPlanController;

    private PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();

    private RequestDTO requestDto = TestDataProvider.getRequestDto();

    private SuccessResponse<PatientTreatmentPlan> response;

    @Test
    @DisplayName("GetTreatmentPlan Test")
    void getPatientTreatmentPlan() {
        //given
        PatientTreatmentPlan actualTreatmentPlan;
        requestDto.setPatientTrackId(1L);
        requestDto.setPatientVisitId(1L);
        //when
        when(treatmentPlanService.getPatientTreatmentPlanDetails(requestDto.getPatientTrackId()))
                .thenReturn(patientTreatmentPlan);
        //then
        response = treatmentPlanController.getPatientTreatmentPlan(requestDto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        actualTreatmentPlan = treatmentPlanService.getPatientTreatmentPlanDetails(requestDto.getPatientTrackId());
        Assertions.assertEquals(requestDto.getPatientVisitId(),
                actualTreatmentPlan.getPatientVisitId());
        Assertions.assertEquals(requestDto.getPatientTrackId(),
                actualTreatmentPlan.getPatientTrackId());
    }

    @Test
    @DisplayName("UpdateTreatmentPlanData Test")
    void updateTreatmentPlanData() {
        //given
        PatientTreatmentPlanDTO patientTreatmentPlanDTO = TestDataProvider.getPatientTreatmentPlanDTO();
        patientTreatmentPlan.setRiskLevel("high");
        //when
        when(treatmentPlanService.updateTreatmentPlanData(patientTreatmentPlan)).thenReturn(true);
        //then
        Assertions.assertNotNull(patientTreatmentPlan);
        SuccessResponse<Boolean> successResponse = treatmentPlanController
                .updateTreatmentPlanData(patientTreatmentPlanDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        boolean actualResult = treatmentPlanService.updateTreatmentPlanData(patientTreatmentPlan);
        Assertions.assertEquals(true, actualResult);
    }
}
