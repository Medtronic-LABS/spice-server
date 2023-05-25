package com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTreatmentPlanDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * Patient treatment plan controller class defines REST API endpoints for retrieving and updating patient treatment
 * plan data.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping("/patient-treatment-plan")
public class PatientTreatmentPlanController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PatientTreatmentPlanService treatmentPlanService;

    /**
     * <p>
     * This method is used to return the patient treatment plan details for a given patient track ID.
     * </p>
     *
     * @param request {@link RequestDTO} The request contains necessary information to retrieve a patient treatment
     *                plan is given
     * @return {@link SuccessResponse<PatientTreatmentPlan>} The SuccessResponse containing a PatientTreatmentPlan,
     * a success code (GET_TREATMENT_PLAN), and an HTTP status code (OK) is returned
     */
    @PostMapping("/details")
    @TenantValidation
    public SuccessResponse<PatientTreatmentPlan> getPatientTreatmentPlan(@RequestBody RequestDTO request) {
        Logger.logDebug("In PatientTreatmentPlanController, get patient treatment plan");
        return new SuccessResponse<>(SuccessCode.GET_TREATMENT_PLAN,
                treatmentPlanService.getPatientTreatmentPlanDetails(request.getPatientTrackId()), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update a patient's treatment plan data and returns a success response.
     * </p>
     *
     * @param patientTreatmentPlanDto {@link PatientTreatmentPlanDTO} The PatientTreatmentPlan contains updated data
     *                                for a patient's treatment plan is given
     * @return {@link SuccessResponse<Boolean>} The success message with status is returned if the patient treatment
     * plan is updated
     */
    @PutMapping("/update")
    @TenantValidation
    public SuccessResponse<Boolean> updateTreatmentPlanData(@Valid @RequestBody
                                                            PatientTreatmentPlanDTO patientTreatmentPlanDto) {
        Logger.logDebug("In PatientTreatmentPlanController, update patient treatment plan");
        treatmentPlanService
                .updateTreatmentPlanData(modelMapper.map(patientTreatmentPlanDto, PatientTreatmentPlan.class));
        return new SuccessResponse<>(SuccessCode.TREATMENT_PLAN_UPDATE, HttpStatus.OK);
    }
}
