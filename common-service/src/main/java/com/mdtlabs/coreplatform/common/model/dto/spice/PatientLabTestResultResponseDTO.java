package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * This is a Java class representing a response object for a patient's lab test results, including
 * comments, result date, and a list of test results.
 * </p>
 */
@Data
public class PatientLabTestResultResponseDTO {
    private String comment;
    private Date resultDate;
    private List<PatientLabTestResult> patientLabTestResults;
}
