package com.mdtlabs.coreplatform.spiceservice.screeninglog.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.OldScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.service.ScreeningLogService;
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
 * ScreeningLogControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in ScreeningLogController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class ScreeningLogControllerTest {

    @InjectMocks
    private ScreeningLogController screeningLogController;

    @Mock
    private ScreeningLogService screeningLogService;

    //given
    private ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();

    private ScreeningLog screeningLog = TestDataProvider.getScreeningLog();

    private RequestDTO requestDTO = TestDataProvider.getRequestDto();

    private ScreeningResponseDTO screeningResponseDTO = TestDataProvider.getScreeningResponseDTO();

    private OldScreeningLogDTO oldScreeningLogDTO = TestDataProvider.getOldScreeningLogDto();

    private SuccessResponse<ScreeningLog> screeningLogSuccessResponse;

    private SuccessResponse<ScreeningResponseDTO> screeningResponseSuccessResponse;

    @Test
    @DisplayName("AddScreeningLog Test")
    void addScreeningLog() {
        //when
        when(screeningLogService.createScreeningLog(screeningLogDTO)).thenReturn(screeningLog);
        //then
        screeningLogSuccessResponse = screeningLogController.addScreeningLog(screeningLogDTO);
        Assertions.assertEquals(HttpStatus.CREATED, screeningLogSuccessResponse.getStatusCode());
        Assertions.assertEquals(screeningLog.getBpLogDetails(),
                screeningLogService.createScreeningLog(screeningLogDTO).getBpLogDetails());
        Assertions.assertEquals(screeningLog, screeningLogService.createScreeningLog(screeningLogDTO));
    }

    @Test
    @DisplayName("GetScreeningLogById Test")
    void getScreeningLogById() {
        //when
        when(screeningLogService.getScreeningDetails(requestDTO)).thenReturn(screeningResponseDTO);
        //then
        screeningResponseSuccessResponse = screeningLogController.getScreeningLogById(requestDTO);
        Assertions.assertEquals(requestDTO.getSiteId(),
                screeningLogService.getScreeningDetails(requestDTO).getSiteId());
        Assertions.assertEquals(HttpStatus.OK, screeningResponseSuccessResponse.getStatusCode());
        Assertions.assertEquals(requestDTO.getTenantId(),
                screeningLogService.getScreeningDetails(requestDTO).getTenantId());
    }
}
