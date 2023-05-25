package com.mdtlabs.coreplatform.spiceservice.bplog.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientBpLogsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.util.ConversionUtil;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * This class is a controller class to perform operation on BpLog entity.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@RestController
@RequestMapping(value = "/bplog")
@Validated
public class BpLogController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private BpLogService bpLogService;

    /**
     * This function creates a new blood pressure log entry and returns a success response with the
     * saved data.
     *
     * @param bpLogDto {@link BpLogDTO} It is a DTO (Data Transfer Object) that contains the data for creating a new
     *                 blood pressure log entry is given
     * @return {@link SuccessResponse<BpLogDTO>} A SuccessResponse object containing a BpLogDTO object with the average systolic and
     * diastolic blood pressure and BMI, along with a success code and HTTP status code is returned
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<BpLogDTO> addBpLog(@Valid @RequestBody BpLogDTO bpLogDto) {
        Logger.logDebug("In BpLogController, creating bp log information");
        BpLog bpLog = modelMapper.map(bpLogDto, BpLog.class);
        if (UnitConstants.IMPERIAL.equals(bpLog.getUnitMeasurement())) {
            ConversionUtil.convertBpLogUnits(bpLog, UnitConstants.IMPERIAL);
        }
        bpLog.setLatest(Constants.BOOLEAN_TRUE);
        bpLog.setType(Constants.MEDICAL_REVIEW);
        BpLog bpLogResponse = bpLogService.addBpLog(bpLog, Constants.BOOLEAN_TRUE);
        return new SuccessResponse<>(SuccessCode.BP_LOG_SAVE,
                new BpLogDTO(bpLogResponse.getAvgSystolic(), bpLogResponse.getAvgDiastolic(), bpLogResponse.getBmi()),
                HttpStatus.CREATED);
    }

    /**
     * Gets bp log information by patient track id
     *
     * @param {@link RequestDTO} patientBpLogsRequestDto request dto is given
     * @return {@link SuccessResponse<PatientBpLogsDTO>} PatientBpLogsDto entity is returned
     */
    @PostMapping("/list")
    @TenantValidation
    public SuccessResponse<PatientBpLogsDTO> getBpLogsByPatientTrackId(
            @RequestBody RequestDTO patientBpLogsRequestDto) {
        Logger.logDebug("In BpLogController, fetching bp logs by patient track id information");
        return new SuccessResponse<>(SuccessCode.GET_BP_LOG_LIST,
                bpLogService.getPatientBpLogsWithSymptoms(patientBpLogsRequestDto), HttpStatus.OK);
    }
}
