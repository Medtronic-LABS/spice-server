package com.mdtlabs.coreplatform.common.model.dto.fhir;

import com.mdtlabs.coreplatform.common.model.entity.spice.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FhirAssessmentRequestDto {
        private @NotNull(
                message = "Type should not be null"
        ) String type;
        private BpLog bpLog;
        private GlucoseLog glucoseLog;
        private MentalHealth mentalHealth;
        private List<PatientSymptom> patientSymptomList;
        private PatientPregnancyDetails patientPregnancyDetails;
        private RedRiskNotification redRiskNotification;
        private List<PatientMedicalCompliance> patientMedicalComplianceList;
        private PatientAssessment patientAssessment;
        private PatientTracker patientTracker;
        private Long createdBy;
        private Long updatedBy;
        private Long patientTrackId;
}