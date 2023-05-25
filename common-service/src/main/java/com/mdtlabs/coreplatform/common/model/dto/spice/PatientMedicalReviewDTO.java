package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * This is a Java class representing a patient's medical review, including physical exams, complaints,
 * comments, and clinical notes.
 * </p>
 */
@Data
@AllArgsConstructor
public class PatientMedicalReviewDTO {

    private Long id;

    private Long patientVisitId;

    private Set<Map<String, String>> physicalExams;

    private Set<Map<String, String>> complaints;

    private String physicalExamComments;

    private String compliantComments;

    private Date reviewedAt;

    private String clinicalNote;
}
