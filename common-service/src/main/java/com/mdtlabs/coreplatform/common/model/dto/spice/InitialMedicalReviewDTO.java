package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComplication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import lombok.Data;

import java.util.Set;

/**
 * <p>
 * The InitialMedicalReviewDTO class contains information about a patient's diagnosis, current
 * medications, comorbidities, complications, and lifestyle.
 * </p>
 */
@Data
public class InitialMedicalReviewDTO {

    private PatientDiagnosis diagnosis;

    private CurrentMedicationDetailsDTO currentMedications;

    private Set<PatientComorbidity> comorbidities;

    private Set<PatientComplication> complications;

    private Set<PatientLifestyle> lifestyle;

    private Boolean isPregnant;

}
