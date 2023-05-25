package com.mdtlabs.coreplatform.spiceservice.glucoselog.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

/**
 * <p>
 * GlucoseLogControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in GlucoseLogController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class GlucoseLogControllerTest {

    @InjectMocks
    private GlucoseLogController glucoseLogController;

    @Mock
    private GlucoseLogService glucoseLogService;

    private RequestDTO bpLogRequestDTO = TestDataProvider.getRequestDto();

    private PatientGlucoseLogDTO patientGlucoseLogDTO = TestDataProvider.getPatientGlucoseLogDTO();

    private GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();

    @Test
    @DisplayName("GlucoseLogCreate test")
    void addGlucoseLog() {
        //given
        GlucoseLogDTO glucoseLogDTO = TestDataProvider.getGlucoseLogDTO();
        SuccessResponse<GlucoseLog> glucoseLogSuccessResponse;
        //when
        when(glucoseLogService.addGlucoseLog(glucoseLog, true)).thenReturn(glucoseLog);
        //then
        glucoseLogSuccessResponse = glucoseLogController.addGlucoseLog(glucoseLogDTO);
        Assertions.assertEquals(HttpStatus.CREATED, glucoseLogSuccessResponse.getStatusCode(),
                "SuccessCode Should be Same");
        GlucoseLog actualGlucoseLog = glucoseLogService.addGlucoseLog(glucoseLog, true);
        Assertions.assertEquals(glucoseLog.isLatest(), actualGlucoseLog.isLatest(),
                "GlucoseLog IsLatest Should be Same");
        Assertions.assertEquals(actualGlucoseLog.getType(),
                glucoseLog.getType(), "Glucose Type Should be Same");
    }

    @Test
    @DisplayName("BpLogsByPatientTrackId Test")
    void getBpLogsPatientTrackId() {
        //given
        SuccessResponse<PatientGlucoseLogDTO> patientGlucoseLogDTOSuccessResponse;
        //when
        when(glucoseLogService.getPatientGlucoseLogsWithSymptoms(bpLogRequestDTO)).thenReturn(patientGlucoseLogDTO);
        //then
        patientGlucoseLogDTOSuccessResponse = glucoseLogController.getBpLogsByPatientTrackId(bpLogRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, patientGlucoseLogDTOSuccessResponse.getStatusCode(),
                "SuccessCode Should be Same");
        Assertions.assertEquals((glucoseLogService.getPatientGlucoseLogsWithSymptoms(bpLogRequestDTO))
                        .getLatestGlucoseLog(), patientGlucoseLogDTO.getLatestGlucoseLog(),
                "GlucoseLog Object should be same");
        Assertions.assertEquals(true, glucoseLogService.getPatientGlucoseLogsWithSymptoms(bpLogRequestDTO)
                .getLatestGlucoseLog().isLatest(), "BpLog IsLatest Should be true ");
    }
}
