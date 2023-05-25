package com.mdtlabs.coreplatform.spiceservice.patientvisit.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * PatientVisitController defines a REST API endpoint for creating a new patient visit and returns a success response
 * with the created patient visit ID.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 09, 2023
 */
@RestController
@RequestMapping("/patientvisit")
public class PatientVisitController {

    @Autowired
    private PatientVisitService patientVisitService;

    /**
     * <p>
     * This method is used to create a new patient visit and returns a success response with the created patient
     * visit ID.
     * </p>
     *
     * @param patientVisitDto {@link RequestDTO} The patient visit contains information about the patient visit is
     *                        given
     * @return {@link SuccessResponse<Map<String, Long>>} The SuccessResponse object containing a map with a single key
     * "PATIENT_VISIT_SAVE" and a value of type Long, which represents the ID of the newly created patient visit.
     * The HTTP status code returned
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<Map<String, Long>> addPatientVisit(@RequestBody RequestDTO
                                                                      patientVisitDto) {
        return new SuccessResponse<>(SuccessCode.PATIENT_VISIT_SAVE,
                patientVisitService.createPatientVisit(patientVisitDto), HttpStatus.CREATED);
    }
}
