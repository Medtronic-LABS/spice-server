package com.mdtlabs.coreplatform.spiceservice.bplog.controller;

import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientBpLogsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.util.spice.SpiceUtil;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * <p>
 * BpLogControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in BpLogController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BpLogControllerTest {

    @InjectMocks()
    private BpLogController bpLogController;

    @Mock()
    private BpLogService bpLogService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(BpLogController.class, "modelMapper", bpLogController);
    }

    private BpLog bpLog = TestDataProvider.getBpLog();

    private RequestDTO requestDTO = TestDataProvider.getRequestDto();

    private PatientBpLogsDTO patientBpLogsDTO = TestDataProvider.getPatientBpLogDTO();

    private SuccessResponse<PatientBpLogsDTO> patientBpLogsDTOSuccessResponse;

    private SuccessResponse<BpLogDTO> bpLogDTOSuccessResponse;

    @Test
    @DisplayName("BpLogSave Test")
    void addBpLog() {
        //given
        BpLogDTO bpLogDTO = TestDataProvider.getBpLogDTO();
        bpLogDTO.setType("medicalreview");
        //when
        when(modelMapper.map(bpLogDTO, BpLog.class)).thenReturn(bpLog);
        when(bpLogService.addBpLog(bpLog, true)).thenReturn(bpLog);
        //then
        bpLogDTOSuccessResponse = bpLogController.addBpLog(bpLogDTO);
        Assertions.assertEquals(HttpStatus.CREATED, bpLogDTOSuccessResponse.getStatusCode());
        Assertions.assertEquals(bpLog.getType(), bpLogService.addBpLog(bpLog, true).getType());
        Assertions.assertEquals(bpLog.getAvgSystolic(),
                bpLogService.addBpLog(bpLog, true).getAvgSystolic());
        Assertions.assertEquals(bpLog.getAvgDiastolic(),
                bpLogService.addBpLog(bpLog, true).getAvgDiastolic());
    }

    @Test
    @DisplayName("AddBpLogWithImperial Test")
    void addBpLogWithImperial() {
        //given
        BpLogDTO bpLogDTO = TestDataProvider.getBpLogDTO();
        bpLogDTO.setType("medicalreview");
        bpLog.setUnitMeasurement(UnitConstants.IMPERIAL);
        bpLogDTO.setUnitMeasurement(UnitConstants.IMPERIAL);
        MockedStatic<SpiceUtil> spiceUtil = mockStatic(SpiceUtil.class);
        //when
        when(modelMapper.map(bpLogDTO, BpLog.class)).thenReturn(bpLog);
        spiceUtil.when(() -> SpiceUtil.convertBpLogUnits(bpLog, UnitConstants.METRIC)).thenReturn(bpLog);
        when(bpLogService.addBpLog(bpLog, true)).thenReturn(bpLog);
        //then
        bpLogDTOSuccessResponse = bpLogController.addBpLog(bpLogDTO);
        spiceUtil.close();
        Assertions.assertEquals(HttpStatus.CREATED, bpLogDTOSuccessResponse.getStatusCode());
        Assertions.assertEquals(bpLog.getType(), bpLogService.addBpLog(bpLog, true).getType());
        Assertions.assertEquals(bpLog.getAvgSystolic(),
                bpLogService.addBpLog(bpLog, true).getAvgSystolic());
        Assertions.assertEquals(bpLog.getAvgDiastolic(),
                bpLogService.addBpLog(bpLog, true).getAvgDiastolic());
    }

    @Test
    @DisplayName("GetBpLogByPatientTrackerId")
    void getBpLogsByPatientTrackerId() {
        //when
        when(bpLogService.getPatientBpLogsWithSymptoms(requestDTO)).thenReturn(patientBpLogsDTO);
        //then
        patientBpLogsDTOSuccessResponse = bpLogController.getBpLogsByPatientTrackId(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, patientBpLogsDTOSuccessResponse.getStatusCode());
        Assertions.assertEquals(patientBpLogsDTO.getLatestBpLog().getPatientTrackId(),
                bpLogService.getPatientBpLogsWithSymptoms(requestDTO).getLatestBpLog().getPatientTrackId());
        Assertions.assertEquals(patientBpLogsDTO, bpLogService.getPatientBpLogsWithSymptoms(requestDTO));

    }
}
