package com.mdtlabs.coreplatform.spiceservice.screeninglog.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.common.util.spice.ScreeningInfo;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.service.ScreeningLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Screening log controller class defines two methods for adding and retrieving screening log information, with
 * validation and logging.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 20, 2022
 */
@RestController
@RequestMapping(value = "/screeninglog")
@Validated
public class ScreeningLogController {

    @Autowired
    private ScreeningLogService screeningLogService;

    /**
     * <p>
     * This method is used to add screening log information and returns a success response.
     * </p>
     *
     * @param screeningLogRequest {@link ScreeningLogDTO} The screening log need to be created is given
     * @return {@link SuccessResponse<ScreeningLog>} Returns a success message and status after the screening
     * log is created
     */
    @TenantValidation
    @PostMapping("/create")
    public SuccessResponse<ScreeningLog> addScreeningLog(
            @Validated(ScreeningInfo.class) @RequestBody ScreeningLogDTO screeningLogRequest) {
        Logger.logInfo("In ScreeningLogController, adding screening information");
        screeningLogService.createScreeningLog(screeningLogRequest);
        return new SuccessResponse<>(SuccessCode.SCREENING_LOG_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to return screening details for a given request DTO object.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information to get the screening response
     *                   is given
     * @return {@link SuccessResponse<ScreeningResponseDTO>} The ScreeningResponse contains screening information is
     * returned with status
     */
    @PostMapping("/details")
    @TenantValidation
    public SuccessResponse<ScreeningResponseDTO> getScreeningLogById(@RequestBody RequestDTO requestDto) {
        Logger.logInfo("In ScreeningLogController, fetching screening information");
        return new SuccessResponse<>(SuccessCode.GET_SCREENING_LOG,
                screeningLogService.getScreeningDetails(requestDto), HttpStatus.OK);
    }
}
