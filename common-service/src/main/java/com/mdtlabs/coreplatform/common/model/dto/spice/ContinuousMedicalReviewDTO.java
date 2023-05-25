package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComplication;
import lombok.Data;

import java.util.Set;


/**
 * <p>
 * The ContinuousMedicalReviewDTO class is a data transfer object that contains information about a
 * patient's physical exams, complaints, comorbidities, complications, and clinical notes.
 * </p>
 */
@Data
public class ContinuousMedicalReviewDTO extends CommonRequestDTO {

    private Set<Long> physicalExams;

    private Set<Long> complaints;

    private Set<PatientComorbidity> comorbidities;

    private Set<PatientComplication> complications;

    private String complaintComments;

    private String physicalExamComments;

    private String clinicalNote;
}
