package com.mdtlabs.coreplatform.spiceservice.glucoselog.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
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

/**
 * <p>
 * This class is a controller class to perform operation on GlucoseLog entity.
 * </p>
 *
 * @author Karthick Murugesan
 * @since Jun 30, 2022
 */
@RestController
@RequestMapping(value = "/glucoselog")
@Validated
public class GlucoseLogController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private GlucoseLogService glucoseLogService;

    /**
     * <p>
     * This method is used to add a new Glucose log.
     * </p>
     *
     * @param glucoseLogDto {@link GlucoseLogDTO} entity
     * @return GlucoseLog {@link SuccessResponse<GlucoseLog>} entity.
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<GlucoseLog> addGlucoseLog(@RequestBody GlucoseLogDTO glucoseLogDto) {
        GlucoseLog glucoseLog = modelMapper.map(glucoseLogDto, GlucoseLog.class);
        glucoseLog.setLatest(Constants.BOOLEAN_TRUE);
        glucoseLog.setType(Constants.MEDICAL_REVIEW);
        glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_TRUE);
        return new SuccessResponse<>(SuccessCode.GLUCOSE_LOG_SAVE, HttpStatus.CREATED);
    }

    /**
     * This method is used to fetch bp logs using patient tracker id.
     *
     * @param patientBpLogsRequestDto {@link RequestDTO} entity
     * @return PatientBpLogsDto       {@link SuccessResponse<PatientGlucoseLogDTO>} entity
     */
    @PostMapping("/list")
    @TenantValidation
    public SuccessResponse<PatientGlucoseLogDTO> getBpLogsByPatientTrackId(@RequestBody RequestDTO
                                                                                   patientBpLogsRequestDto) {
        return new SuccessResponse<>(
                SuccessCode.GET_GLUCOSE_LOG_LIST,
                glucoseLogService.getPatientGlucoseLogsWithSymptoms(patientBpLogsRequestDto),
                HttpStatus.OK
        );
    }
}
