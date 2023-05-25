package com.mdtlabs.coreplatform.spiceservice.patienttracker.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.ConfirmDiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * This class is a controller class to perform operation on Patient Tracker
 * entity.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@RestController
@RequestMapping("/patient")
public class PatientTrackerController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PatientTrackerService patientTrackerService;

    /**
     * <p>
     * This method is used to add a new patient tracker.
     * </p>
     *
     * @param patientTrackerDto {@link PatientTrackerDTO} entity
     * @return {@link SuccessResponse<PatientTracker>} PatientTracker Entity.
     */
    @PostMapping
    @TenantValidation
    public SuccessResponse<PatientTracker> addPatientTracker(@RequestBody PatientTrackerDTO patientTrackerDto) {
        patientTrackerService.addOrUpdatePatientTracker(modelMapper.map(patientTrackerDto, PatientTracker.class));
        return new SuccessResponse<>(SuccessCode.PATIENT_TRACKER_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to retrieve single patient tracker using
     * patientTrackerId.
     * </p>
     *
     * @param patientTrackerId {@link long} patient tracker id
     * @return {@link SuccessResponse<PatientTracker>} PatientTracker Entity
     */
    @GetMapping("/{id}")
    public SuccessResponse<PatientTracker> getPatientTrackerById(
            @PathVariable(value = Constants.ID) long patientTrackerId) {
        return new SuccessResponse<>(SuccessCode.GET_PATIENT_TRACKER,
                patientTrackerService.getPatientTrackerById(patientTrackerId), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function updates a confirm diagnosis record for a patient in a medical tracking
     * system.
     * </p>
     *
     * @param confirmDiagnosis {@link ConfirmDiagnosisDTO} The parameter `confirmDiagnosis` is a `ConfirmDiagnosisDTO` object that
     *                         is received in the request body of a PATCH request
     * @return {@link SuccessResponse<ConfirmDiagnosisDTO>} A SuccessResponse object containing a ConfirmDiagnosisDTO object, with a success code
     * and HTTP status of OK
     */
    @PatchMapping("/confirm-diagnosis/update")
    @TenantValidation
    public SuccessResponse<ConfirmDiagnosisDTO> updateConfirmDiagnosis(
            @RequestBody ConfirmDiagnosisDTO confirmDiagnosis) {
        return new SuccessResponse<>(SuccessCode.CONFIRM_DIAGNOSIS_UPDATE,
                patientTrackerService.updateConfirmDiagnosis(confirmDiagnosis), HttpStatus.OK);
    }

}
