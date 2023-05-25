package com.mdtlabs.coreplatform.spiceservice.assessment.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.spiceservice.assessment.service.AssessmentService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
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

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * AssessmentControllerTest class is used to test each and every method
 * for both positive and negative with respective scenarios.
 * <p/>
 *
 * @author Jaganathan.R created on Jan 30 2023.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AssessmentControllerTest {

    @InjectMocks()
    private AssessmentController assessmentController;

    @Mock()
    private AssessmentService assessmentService;

    private SuccessResponse<String> stringSuccessResponse;

    private SuccessResponse<AssessmentResponseDTO> assessmentResponseDTOSuccessResponse;

    @Test
    @DisplayName("Assessment create test")
    void createAssessment() {
        //given
        AssessmentDTO assessmentDTO = TestDataProvider.getAssessmentDto();
        AssessmentResponseDTO responseDTO = TestDataProvider.getAssessmentResponseDto();
        //when
        when(assessmentService.createAssessment(assessmentDTO)).thenReturn(responseDTO);
        //then
        assessmentResponseDTOSuccessResponse = assessmentController.createAssessment(assessmentDTO);
        Assertions.assertNotNull(assessmentResponseDTOSuccessResponse, "Response Should Not be Null");
        Assertions.assertEquals(assessmentService.createAssessment(assessmentDTO).getPatientDetails().getId(),
                responseDTO.getPatientDetails().getId(), "Failed unMatches of objects");
        Assertions.assertEquals(HttpStatus.CREATED, assessmentResponseDTOSuccessResponse.getStatusCode(),"failed UnMatches status Codes");
    }

    @Test
    @DisplayName("CreateBpLog test")
    void createAssessmentBpLog() {
        //given
        BpLog bpLog = TestDataProvider.getBpLog();
        BpLogDTO bpLogDTO = TestDataProvider.getBpLogDTO();
        //when
        when(assessmentService.createAssessmentBpLog(bpLog)).thenReturn(bpLog);
        //then
        stringSuccessResponse = assessmentController.createBpLogAssessment(bpLogDTO);
        BpLog actualBpLog = assessmentService.createAssessmentBpLog(bpLog);
        Assertions.assertEquals(HttpStatus.CREATED, stringSuccessResponse.getStatusCode(),
                "Failed UnMatches status code");
        Assertions.assertEquals(bpLog.getCvdRiskLevel(),
                actualBpLog.getCvdRiskLevel());
        Assertions.assertEquals(bpLog.getBmi(), actualBpLog.getBmi());

    }


    @Test
    @DisplayName("Create AssessmentGlucoseLog Test")
    void createGlucoseLogAssessment() {
        //given
        GlucoseLogDTO glucoseLogDTO = TestDataProvider.getGlucoseLogDTO();
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        //when
        when(assessmentService.createAssessmentGlucoseLog(glucoseLog)).thenReturn(glucoseLog);
        //then
        stringSuccessResponse = assessmentController.createGlucoseLogAssessment(glucoseLogDTO);
        GlucoseLog actualGlucoseLog = assessmentService.createAssessmentGlucoseLog(glucoseLog);
        Assertions.assertEquals(HttpStatus.CREATED, stringSuccessResponse.getStatusCode(), "Failed due to UnMatches of Status Code");
        Assertions.assertEquals(glucoseLog.getPatientTrackId(), actualGlucoseLog.getPatientTrackId(),
                "failed UnMatches of PatientTrackerId");
        Assertions.assertEquals(glucoseLog.getGlucoseLogId(), actualGlucoseLog.getGlucoseLogId());
        Assertions.assertEquals(glucoseLog.getType(), actualGlucoseLog.getType());
    }

    @Test
    @DisplayName("clearApi Test")
    void clearApiPermissions() {
        lenient().doNothing().when(assessmentService).clearApiPermissions();
        assessmentController.clearApiPermissions();
        verify(assessmentService, atLeastOnce()).clearApiPermissions();
    }
}
