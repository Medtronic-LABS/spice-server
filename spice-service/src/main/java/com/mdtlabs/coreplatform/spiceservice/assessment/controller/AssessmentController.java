package com.mdtlabs.coreplatform.spiceservice.assessment.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.spiceservice.assessment.service.AssessmentService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * This class is a controller class to perform operation on Assessment
 * operations.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@RestController
@RequestMapping(value = "/assessment")
@Validated
public class AssessmentController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AssessmentService assessmentService;

    /**
     * <p>
     * This is a Java function that creates an assessment and returns a success response with the
     * created assessment information.
     * </p>
     *
     * @param assessmentRequest {@link AssessmentDTO} assessment dto is given
     * @return {@link SuccessResponse<AssessmentResponseDTO>} AssessmentDto Entity is returned
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<AssessmentResponseDTO> createAssessment(@Valid @RequestBody
                                                                   AssessmentDTO assessmentRequest) {
        Logger.logInfo("In AssessmentController, creating assessment information");
        return new SuccessResponse<>(SuccessCode.ASSESSMENT_SAVE,
                assessmentService.createAssessment(assessmentRequest), HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to add a new BpLog Assessment.
     * </p>
     *
     * @param bpLogDto {@link BpLogDTO} entity is given
     * @return {@link SuccessResponse<String>} Success Message is returned
     */
    @PostMapping("/bplog-create")
    @TenantValidation
    public SuccessResponse<String> createBpLogAssessment(@Valid @RequestBody BpLogDTO bpLogDto) {
        Logger.logInfo("In AssessmentController, creating bp log assessment information");
        assessmentService.createAssessmentBpLog(modelMapper.map(bpLogDto, BpLog.class));
        return new SuccessResponse<>(SuccessCode.ASSESSMENT_BP_LOG_SAVE, HttpStatus.CREATED);

    }

    /**
     * <p>
     * This method is used to add a new Glucose Log Assessment.
     * </p>
     *
     * @param glucoseLogDto {@link GlucoseLogDTO} entity is given
     * @return {@link SuccessResponse<String>} success response is returned
     */
    @PostMapping("/glucoselog-create")
    @TenantValidation
    public SuccessResponse<String> createGlucoseLogAssessment(@Valid @RequestBody GlucoseLogDTO glucoseLogDto) {
        Logger.logInfo("In AssessmentController, creating glucose log assessment information");
        assessmentService.createAssessmentGlucoseLog(modelMapper.map(glucoseLogDto, GlucoseLog.class));
        return new SuccessResponse<>(SuccessCode.ASSESSMENT_GLUCOSE_LOG_SAVE, HttpStatus.CREATED);

    }

    /**
     * <p>
     * This method is used to clear the api permission role map.
     * </p>
     */
    @GetMapping(value = "/clear")
    public void clearApiPermissions() {
        Logger.logInfo("In AssessmentController, clear api permission");
        assessmentService.clearApiPermissions();
    }

}
